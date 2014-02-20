//
//  BPLocation.h
//  BuddySDK
//
//  Created by Erik Kerber on 1/9/14.
//
//

@protocol BPLocationProperties <BuddyObjectProperties>

@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *description;
@property (nonatomic, copy) NSString *streetAddress;
@property (nonatomic, copy) NSString *city;
@property (nonatomic, copy) NSString *state;
@property (nonatomic, copy) NSString *country;
@property (nonatomic, copy) NSString *zipcode;
@property (nonatomic, copy) NSString *fax;
@property (nonatomic, copy) NSString *phone;
@property (nonatomic, copy) NSString *website;
@property (nonatomic, copy) NSString *categoryId;
@property (nonatomic, assign) double *distance;

@end

typedef void(^DescribeLocation)(id<BPLocationProperties>locationProperties);

@interface BPLocation : BuddyObject

@end
