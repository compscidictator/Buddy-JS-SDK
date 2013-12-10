//
//  BPCheckinCollection.h
//  BuddySDK
//
//  Created by Erik Kerber on 11/15/13.
//
//

#import "BuddyCollection.h"
#import "BPCheckin.h"
#import "BPCoordinate.h"

@interface BPCheckinCollection : BuddyCollection

// Hmm, I don't like these parameter-heavy methods.
-(void)checkinWithComment:(NSString *)comment
          description:(NSString *)description
             //location:(struct BPCoordinate)coordinate
      defaultMetadata:(NSString *)defaultMetadata
      readPermissions:(BuddyPermissions)readPermissions
     writePermissions:(BuddyPermissions)writePermissions
                 complete:(BuddyObjectCallback)complete;

-(void)checkinWithComment:(NSString *)comment
              description:(NSString *)description
                 complete:(BuddyObjectCallback)complete;

-(void)getCheckins:(BuddyCollectionCallback)complete;

// I think I like this? Create/populate object->dispatch away
-(void)addCheckin:(BPCheckin *)checkin;



@end
