//
//  BuddyObject.m
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import "BuddyObject.h"
#import "BuddyObject+Private.h"
#import "BPSession.h"

#import "JAGPropertyConverter.h"
#import "BPSession.h"
#import "BPCoordinate.h"
#import "NSDate+JSON.h"

@interface BuddyObject()

@property (nonatomic, readwrite, assign) BOOL isDirty;
@property (nonatomic, strong) NSMutableArray *keyPaths;

@end


@implementation BuddyObject

@synthesize session=_session;


#pragma mark - Initializers

- (void)dealloc
{
    for(NSString *keypath in self.keyPaths)
    {
        [self removeObserver:self forKeyPath:keypath];
    }
}


- (instancetype)initBuddyWithSession:(BPSession*)session
{
    self = [super init];
    if(self)
    {
        _session=session;
        [self registerProperties];
    }
    return self;
}

- (instancetype)initBuddyWithResponse:(id)response andSession:(BPSession*)session
{
    if (!response) return nil;
    
    self = [super init];
    if(self)
    {
        _session = session;
        [self registerProperties];
        [[[self class] converter] setPropertiesOf:self fromDictionary:response];
    }
    return self;
}

-(BPSession*)session
{
    if(_session!=nil)
    {
        return _session;
    }
    
    return [BPSession currentSession];
    
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

+ (NSDictionary *)enumMap
{
    // Return any enum->string mappings used in responses subclass.
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

+(void)createFromServerWithParameters:(NSDictionary *)parameters session:(BPSession*)session callback:(BuddyObjectCallback)callback
{
    [[session restService] POST:[[self class] requestPath] parameters:parameters callback:^(id json, NSError *error) {
        
        if (error) {
            callback(nil, error);
            return;
        }
        
        BuddyObject *newObject = [[[self class] alloc] initBuddyWithSession:session];

#pragma messsage("TODO - Short term hack until response is always an object.")
        if ([json isKindOfClass:[NSDictionary class]]) {
            newObject.id = json[@"id"];
        } else {
            newObject.id = json;
        }
        
        [newObject refresh:^(NSError *error){
            callback(newObject, error);
        }];
    }];
}

+(void)queryFromServerWithId:(NSString *)identifier session:(BPSession*)session callback:(BuddyObjectCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          identifier];
    
    [[session restService] GET:resource parameters:nil callback:^(id json, NSError *error) {

        BuddyObject *newObject = [[[self class] alloc] initBuddyWithSession:session];
        newObject.id = json[@"id"];
        
        [[[self class] converter] setPropertiesOf:newObject fromDictionary:json];
#pragma messsage("TODO - Error")
        callback(newObject, nil);
    }];
}

-(void)deleteMe
{
    [self deleteMe:nil];
}

-(void)deleteMe:(BuddyCompletionCallback)callback
{
#pragma message("Figure out clean way to know when to use /me and /id")
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          _id];
    
    [[[BPSession currentSession] restService] DELETE:resource parameters:nil callback:^(id json, NSError *error) {
        if(callback)
            callback(error);
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
    
    [[[BPSession currentSession] restService] GET:resource parameters:nil callback:^(id json, NSError *error) {
        [[[self class] converter] setPropertiesOf:self fromDictionary:json];
        if(callback)
            callback(error);
    }];
}

- (void)save:(BuddyCompletionCallback)callback
{
    // /<resourcePath>/<id>
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          self.id];
    
    // Dictionary of property names/values
    NSDictionary *parameters = [self buildUpdateDictionary];

    [[[BPSession currentSession] restService] PATCH:resource parameters:parameters callback:^(id json, NSError *error) {
        [[[self class] converter] setPropertiesOf:self fromDictionary:json];
        if(callback)
            callback(error);
    }];
}

#pragma mark - JSON handling

+(JAGPropertyConverter *)converter
{
    static JAGPropertyConverter *c;
    if(!c)
    {
        c = [JAGPropertyConverter new];
        
        // TODO - necessary?
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
