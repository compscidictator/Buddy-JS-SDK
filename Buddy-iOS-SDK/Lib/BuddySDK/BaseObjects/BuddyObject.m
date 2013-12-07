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

+(instancetype)create
{
    [[BPClient defaultClient] createObjectWithPath:[[self class] requestPath] parameters:nil complete:^(id json) {
        
    }];
    return nil;
}

+(void)createFromServerWithParameters:(NSDictionary *)parameters complete:(BuddyObjectCallback)callback
{
    [[BPClient defaultClient] createObjectWithPath:[[self class] requestPath] parameters:parameters complete:^(id json) {
        
        BuddyObject *newObject = [[[self class] alloc] initBuddy];
        newObject.identifier = json[@"result"][@"id"];
        
        [[[self class] converter] setPropertiesOf:newObject fromDictionary:json];
        callback(newObject);
    }];
}

+(void)queryFromServerWithId:(NSString *)identifier callback:(BuddyObjectCallback)callback
{
    [[BPClient defaultClient] queryObjectWithPath:[[self class] requestPath] identifier:identifier complete:^(id json) {
        
        BuddyObject *newObject = [[[self class] alloc] initBuddy];
        newObject.identifier = json[@"result"][@"id"];
        
        [[[self class] converter] setPropertiesOf:newObject fromDictionary:json];
        callback(newObject);
    }];
}

-(void)deleteMe
{
    [self deleteMe:nil];
}

-(void)deleteMe:(void(^)())complete
{
    [[BPClient defaultClient] deleteObject:self complete:^(id json) {
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
    [[BPClient defaultClient] refreshObject:self complete:^(id json) {
        [[[self class] converter] setPropertiesOf:self fromDictionary:json[@"result"]];
        if(complete)
            complete();
    }];
}

-(void)update
{
    [[BPClient defaultClient] updateObject:self complete:^(id json) {
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
