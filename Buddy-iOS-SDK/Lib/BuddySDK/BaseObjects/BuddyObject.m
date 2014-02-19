//
//  BuddyObject.m
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import "BuddyObject.h"
#import "BuddyObject+Private.h"

#import "JAGPropertyConverter.h"
#import "BPRestProvider.h"
#import "BPClient.h"
#import "BPCoordinate.h"
#import "NSDate+JSON.h"
#import "BPEnumMapping.h"

@interface BuddyObject()

@property (nonatomic, readwrite, assign) BOOL isDirty;
@property (nonatomic, strong) NSMutableArray *keyPaths;

@end


@implementation BuddyObject

@synthesize client=_client;


#pragma mark - Initializers

- (void)dealloc
{
    for(NSString *keypath in self.keyPaths)
    {
        [self removeObserver:self forKeyPath:keypath];
    }
}


- (instancetype)initBuddyWithClient:(id<BPRestProvider>)client
{
    self = [super init];
    if(self)
    {
        client=client;
        [self registerProperties];
    }
    return self;
}

- (instancetype)initBuddyWithResponse:(id)response andClient:(id<BPRestProvider>)client
{
    if (!response) return nil;
    
    self = [super init];
    if(self)
    {
        _client = client;
        [self registerProperties];
        [[[self class] converter] setPropertiesOf:self fromDictionary:response];
    }
    return self;
}

- (instancetype)initForCreation
{
    self = [super init];
    if(self)
    {
        [self registerProperties];
    }
    return self;
}

- (id<BPRestProvider>)client
{
    return _client ?: (id<BPRestProvider>)[BPClient defaultClient];
}

- (void)registerProperties
{
    self.keyPaths = [NSMutableArray array];
    [self registerProperty:@selector(created)];
    [self registerProperty:@selector(lastModified)];
    [self registerProperty:@selector(defaultMetadata)];
    [self registerProperty:@selector(id)];
}

+(NSString *)requestPath
{
    [NSException raise:@"requestPathNotSpecified" format:@"Class did not specify requestPath"];
    return nil;
}


-(void)registerProperty:(SEL)property
{
    NSString *propertyName = NSStringFromSelector(property);
    
    [self.keyPaths addObject:propertyName];
    
    [self addObserver:self forKeyPath:propertyName options:NSKeyValueObservingOptionNew context:NULL];
}

-(NSDictionary *)buildUpdateDictionary
{
    NSMutableDictionary *buddyPropertyDictionary = [NSMutableDictionary dictionary];
    for (NSString *key in self.keyPaths)
    {
        id c = [self valueForKeyPath:key];
        if (!c) continue;
        
        if([[c class] isSubclassOfClass:[NSDate class]]){
            c = [c serializeDateToJson];
        } else if ([c respondsToSelector:@selector(stringValue)]) {
            c = [c stringValue];
        }
        
        [buddyPropertyDictionary setObject:c forKey:key];
    }
    
    return buddyPropertyDictionary;
}

-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    if(change)
        self.isDirty = YES;
}

#pragma mark CRUD

+(void)createFromServerWithParameters:(NSDictionary *)parameters client:(id<BPRestProvider>)client callback:(BuddyObjectCallback)callback
{
    [client POST:[[self class] requestPath] parameters:parameters callback:^(id json, NSError *error) {
        
        if (error) {
            callback ? callback(nil, error) : nil;
            return;
        }
        
        BuddyObject *newObject = [[[self class] alloc] initBuddyWithClient:client];

        newObject.id = json[@"id"];
        
        [newObject refresh:^(NSError *error){
            callback ? callback(newObject, error) : nil;
        }];
    }];
}

+(void)queryFromServerWithId:(NSString *)identifier client:(id<BPRestProvider>)client callback:(BuddyObjectCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          identifier];
    
    [client GET:resource parameters:nil callback:^(id json, NSError *error) {

        BuddyObject *newObject = [[[self class] alloc] initBuddyWithClient:client];
        newObject.id = json[@"id"];
        
        [[[self class] converter] setPropertiesOf:newObject fromDictionary:json];
#pragma messsage("TODO - Error")
        callback ? callback(newObject, nil) : nil;
    }];
}

-(void)deleteMe
{
    [self deleteMe:nil];
}

-(void)deleteMe:(BuddyCompletionCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          _id];
    
    [self.client DELETE:resource parameters:nil callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

-(void)refresh
{
    [self refresh:nil];
}

-(void)refresh:(BuddyCompletionCallback)callback
{
    assert(self.id);
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          self.id];
    
    [self.client GET:resource parameters:nil callback:^(id json, NSError *error) {
        [[[self class] converter] setPropertiesOf:self fromDictionary:json];
        callback ? callback(error) : nil;
    }];
}

- (void)save:(BuddyCompletionCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          self.id];
    
    // Dictionary of property names/values
    NSDictionary *parameters = [self buildUpdateDictionary];

    [self.client PATCH:resource parameters:parameters callback:^(id json, NSError *error) {
        callback ? callback(error) : nil;
    }];
}

#pragma mark - Metadata

static NSString *metadataRoute = @"metadata";
- (NSString *) metadataPath:(NSString *)key
{
    if(key==nil)
    {
        return [NSString stringWithFormat:@"%@/%@",metadataRoute,self.id];
    }
    return [NSString stringWithFormat:@"%@/%@/%@",metadataRoute,self.id,key];
}


#pragma mark - JSON handling

+(JAGPropertyConverter *)converter
{
    static JAGPropertyConverter *c;
    if(!c)
    {
        c = [JAGPropertyConverter new];
        
        __weak typeof(self) weakSelf = self;
        c.identifyDict = ^Class(NSDictionary *dict) {
            if ([dict valueForKey:@"latitude"]) {
                return [BPCoordinate class];
            }
            return [weakSelf class];
        };
        
    }
    return c;
}

@end
