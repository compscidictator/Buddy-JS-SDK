/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


#import <Foundation/Foundation.h>

#import "BuddyObject.h"

/// <summary>
/// Represents a single message that one user sent to another.
/// </summary>

@interface BuddyMessage : BuddyObject

/// <summary>
/// Gets the DateTime the message was sent.
/// </summary>
@property (readonly, nonatomic, strong) NSDate *dateSent;

/// <summary>
/// Gets the ID of the user who sent the message.
/// </summary>
@property (readonly, nonatomic, strong) NSNumber *fromUserId;

/// <summary>
/// Gets the ID of the user who received the message.
/// </summary>
@property (readonly, nonatomic, strong) NSNumber *toUserId;

/// <summary>
/// Gets the text value of the message.
/// </summary>
@property (readonly, nonatomic, strong) NSString *text;

@end