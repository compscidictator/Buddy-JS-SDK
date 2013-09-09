# Buddy SDK

## Introduction

Buddy enables developers to build engaging, cloud-connected apps without having to write, test, manage or scale server-side code or infrastructure. We noticed that most mobile app developers end up writing the same code over and over again.  User management, photo management, geolocation checkins, metadata, and more.  

Buddy's easy-to-use, scenario-focused APIs let you spend more time building your app, and less time worrying about backend infrastructure.  

Let us handle that stuff for you!

## Features

For developers the Buddy Platform offers turnkey support for features like the following:

* *User Accounts* - create, delete, authenticate users.
* *Photos* - add photos, search photos, share photos with other users.
* *GeoLocation* - checkin, search for places, list past checkins.
* *Push Notifications* - easily send push notifications to iOS, Android, or Microsoft devices.
* *Messaging* - send messages to other users, create message groups.
* *Friends* - set up social relationships between users with friends lists.
* *Game Scores, Metadata, and Boards* - Keep track of game stores and states for individual users as well as across users.
* *Commerce* - Offer items for in-app purchase via Facebook Commerce.
* *And more* - Checkout the rest of the offering at [buddy.com/developers](http://buddy.com/developers/).
* 
## Platform Support

Under the ```src``` directory, you'll find a set of .csproj files for building various flavors of the Buddy Platform SDK

| Platform           | Project File                  |  Notes                                       |
| ------------------ | ----------------------------- | -------------------------------------------- |
| .NET 4.0, Mono Mac | src/Buddy.Net40.csproj        |                                              | 
| .NET 4.5           | src/Buddy.Net45.csproj        |                                              |
| .NET Portable      | src/Buddy.Portable.csproj     | .NET 4.5 Core (Windows Store) + SL5 + WP8    |
| Windows Phone 7.x  | src/Buddy.WP7.csproj          |                                              |
| Windows Phone 8    | src/Buddy.WP8.csproj          |                                              |
| Windows Store      | src/Buddy.WindowsStore.csproj | .NET 4.5 Core                                |
| Xamarin.iOS        | src/Buddy.iOS.csproj          |                                              |                                                 
| Xamarin.Android    | src/Buddy.Android.csproj      |                                              |

All of these project files reference the same source files.  They are purely vehicles for specifying a different project type guid, and set some defines that vary how the code is defined between the platforms.

The principal code differences are:

* The Windows Phone variants support some Windows Phone specific functions (Push, Tiles, etc).
* All variants except Portable and Windows Phone 7.x use the modern Task<T> syntax for async calls.  For platforms that support ***async / await**, this is supported as well.

## How It works

Getting rolling with the Buddy SDK is very easy.  First you'll need to go to [dev.buddy.com](http://dev.buddy.com), to create an account and an application.  This will create an application entry and a key pair consisting of an *Application Name* and an *Application Password*.

Once you have those, you just create a BuddyClient instance, and call methods on it.

Below is some code showing the creation of a user, then uploading a profile photo for that user.

### .NET 4.0 Code Sample

    using Buddy;
    
    // ...
    
    // create the client
    var client = new BuddyClient("MyApplicationName", "MyApplicationPassword");
    
    // create a user
    client.CreateUserAsync("username","password").ContinueWith(r => {
        var user = r.Result;
        
        // upload a profile photo
        Stream photoStream = GetSomePhoto();
        user.AddProfilePhotoAsync(photoStream);
    });
    

### .NET 4.5 Code Sample

    using Buddy;
    
    // ...
    
    // create the client
    var client = new BuddyClient("MyApplicationName", "MyApplicationPassword");
    
    // create a user
    var user = await client.CreateUserAsync("username","password");

    // upload a profile photo
    Stream photoStream = GetSomePhoto();
    var photoId = await user.AddProfilePhotoAsync(photoStream);
    


## Docs

Full documentation for Buddy's services are available at [Buddy.com](http://buddy.com/documentation)

## Contributing Back: Pull Requests

We'd love to have your help making the Buddy SDK as good as it can be!

To submit a fix to the Buddy SDK please do the following:

1. Create your own fork of the Buddy SDK
2. Make the fix to your fork
3. Before creating your pull request, please sync your repo to the current state of the parent repo: ```git pull --rebase origin master```
4. Commit your changes, then [submit a pull request](https://help.github.com/articles/using-pull-requests) for just that commit.


## License

#### Copyright (C) 2012 Buddy Platform, Inc.


Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

  [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.

