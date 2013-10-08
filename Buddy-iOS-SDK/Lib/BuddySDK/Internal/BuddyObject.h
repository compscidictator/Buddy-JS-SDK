//
//  BuddyObject.h
//  BuddySDK
//
//  Created by Erik Kerber on 9/11/13.
//
//

#import <Foundation/Foundation.h>

@interface BuddyObject : NSObject

-(id)initWithExternalRepresentation:(NSDictionary *)json;

@property (nonatomic, readonly, assign) BOOL isDirty;
@property (nonatomic, strong) NSDate *created;
@property (nonatomic, strong) NSDate *lastModified;
@property (nonatomic, strong) NSString *tag;
@property (nonatomic, strong) NSNumber *userId;

-(void)save;


@end
