//
//  BuddyObject.m
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import "BuddyObject.h"

@interface BuddyObject()

@property (nonatomic, readwrite, assign) BOOL isDirty;
@property (nonatomic, strong) NSMutableArray *keyPaths;

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
    }
    return self;
}

-(id)initWithExternalRepresentation:(NSDictionary *)json
{
    self = [super init];
    if(self)
    {
        self.keyPaths = [NSMutableArray array];
        [self registerProperty:@selector(created)];
        [self registerProperty:@selector(lastModified)];
        [self registerProperty:@selector(tag)];
        [self registerProperty:@selector(userId)];
    }
    return self;
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

#pragma mark Override methods

-(void)save
{
    return;
}

@end
