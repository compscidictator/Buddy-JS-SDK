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
#import "BPClient.h"

@interface BuddyObject()

@property (nonatomic, readwrite, assign) BOOL isDirty;
@property (nonatomic, strong) NSMutableArray *keyPaths;

@end

@implementation BuddyObject

+(JAGPropertyConverter *)converter
{
    static JAGPropertyConverter *c;
    if(!c)
    {
        c = [JAGPropertyConverter new];
        
        // TODO - necessary?
        __weak typeof(self) weakSelf = self;
        c.identifyDict = ^Class(NSDictionary *dict) {
            if ([dict valueForKey:@"userID"]) {
                return [weakSelf class];
            }
            return [weakSelf class];
        };
        
    }
    return c;
}

-(instancetype)initBuddy
{
    self = [super init];
    if(self)
    {
        self.keyPaths = [NSMutableArray array];
        [self registerProperty:@selector(created)];
        [self registerProperty:@selector(lastModified)];
        [self registerProperty:@selector(defaultMetadata)];
        [self registerProperty:@selector(userId)];
        [self registerProperty:@selector(identifier)];
    }
    return self;
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

-(void)dealloc
{
    for(NSString *keypath in self.keyPaths)
    {
        [self removeObserver:self forKeyPath:keypath];
    }
}

-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    if(change)
        self.isDirty = YES;
}

#pragma mark CRUD

+(void)createFromServerWithParameters:(NSDictionary *)parameters complete:(BuddyObjectCallback)callback
{
    [[[BPClient defaultClient] restService] POST:[[self class] requestPath] parameters:parameters callback:^(id json) {
        
        BuddyObject *newObject = [[[self class] alloc] initBuddy];

#pragma warning TODO - Short term hack until response is always an object.
        if([json isKindOfClass:[NSDictionary class]]){
            newObject.id = json[@"id"];
        }else{
            newObject.id = json;
        }
        
        if(!newObject.id){
#pragma warning TODO - Error
            callback(newObject, nil);
            return;
        }
        
        [newObject refresh:^(NSError *error){
#pragma warning TODO - Error
            callback(newObject, nil);
        }];
    }];
}

+(void)queryFromServerWithId:(NSString *)identifier callback:(BuddyObjectCallback)callback
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          identifier];
    
    [[[BPClient defaultClient] restService] GET:resource parameters:nil callback:^(id json) {

        BuddyObject *newObject = [[[self class] alloc] initBuddy];
        newObject.id = json[@"id"];
        
        [[[self class] converter] setPropertiesOf:newObject fromDictionary:json];
#pragma warning TODO - Error
        callback(newObject, nil);
    }];
}

-(void)deleteMe
{
    [self deleteMe:nil];
}

-(void)deleteMe:(void(^)())complete
{
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          self.id];
    
    [[[BPClient defaultClient] restService] DELETE:resource parameters:nil callback:^(id json) {
        if(complete)
            complete();
    }];
}

-(void)refresh
{
    [self refresh:nil];
}

-(void)refresh:(BuddyCompletionCallback)complete
{
    assert(self.id);
    NSString *resource = [NSString stringWithFormat:@"%@/%@",
                          [[self class] requestPath],
                          self.id];
    
    [[[BPClient defaultClient] restService] GET:resource parameters:nil callback:^(id json) {
        [[[self class] converter] setPropertiesOf:self fromDictionary:json];
        if(complete)
#pragma warning TODO - NSError
            complete(nil);
    }];
}

-(void)update
{
    NSString *resource = @"TODO - no update API's available yet";

    [[[BPClient defaultClient] restService] POST:resource parameters:nil callback:^(id json) {
        [[[self class] converter] setPropertiesOf:self fromDictionary:json];
    }];
}

#pragma mark Abstract implementors
// "Abstract" methods.
-(NSDictionary *)buildUpdateDictionary
{
    // Abstract
    return nil;
}

-(void)updateObjectWithJSON:(NSString *)json
{
    // Abstract
}
@end
