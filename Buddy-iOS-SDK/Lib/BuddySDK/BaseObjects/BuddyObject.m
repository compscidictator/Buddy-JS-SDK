//
//  BuddyObject.m
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import "BuddyObject.h"
#import "JAGPropertyConverter.h"
#import "BPClient.h"

@interface BuddyObject()

@property (nonatomic, readwrite, assign) BOOL isDirty;
@property (nonatomic, strong) NSMutableArray *keyPaths;
@property (nonatomic, strong) JAGPropertyConverter *converter;

@end

@implementation BuddyObject

-(id)init
{
    self = [super init];
    if(self)
    {
        self.keyPaths = [NSMutableArray array];
        [self registerProperty:@selector(created)];
        [self registerProperty:@selector(lastModified)];
        [self registerProperty:@selector(tag)];
        [self registerProperty:@selector(userId)];
        [self registerProperty:@selector(identifier)];
        
        // Setup property converter.
        self.converter = [JAGPropertyConverter new];
        // TODO - necessary?
        __weak typeof(self) weakSelf = self;
        _converter.identifyDict = ^Class(NSDictionary *dict) {
            if ([dict valueForKey:@"userID"]) {
                return [weakSelf class];
            }
            return [weakSelf class];
        };
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
    [[BPClient defaultClient] createObjectWithPath:[[self class] requestPath] parameters:nil withCallback:^(id json) {
        
    }];
    // TODO - shared instance.
    //[self.client deleteRequest:self.resourceString withId:self.identifier];
    return nil;
}

-(void)deleteMe
{
//    [self.client deleteRequest:self.resourceString withId:self.identifier callback:^(BuddyCallbackParams *callbackParams, id jsonString) {
//        // TODO - Anything?
//    }];
}

-(void)refresh
{
//    [self.client getRequest:self.resourceString withId:self.identifier callback:^(BuddyCallbackParams *callbackParams, id jsonString) {
//        [self.converter setPropertiesOf:self fromDictionary:jsonString];
//    }];
}

-(void)update
{
//    [self.client updateRequest:self.resourceString withId:self.identifier payload:[self buildUpdateDictionary] callback:^(BuddyCallbackParams *callbackParams, id jsonString) {
//        [self.converter setPropertiesOf:self fromDictionary:jsonString];
//    }];
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
