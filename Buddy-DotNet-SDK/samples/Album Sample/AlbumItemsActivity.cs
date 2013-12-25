using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Database;

using BuddySDK;

namespace AlbumsSample
{
	[Activity (Label = "Album Items", Icon = "@drawable/icon")]			
	public class AlbumItemsActivity : Activity
	{
		private const int PickImageId = 1000;

		protected override void OnCreate (Bundle bundle)
		{
			base.OnCreate (bundle);

			SetContentView (Resource.Layout.AlbumItems);

			InitializeAddPictureButton ();
		}

		private void InitializeAddPictureButton()
		{
			var addPictureButton = FindViewById<Button>(Resource.Id.addPictureButton);

			addPictureButton.Click += (object sender, EventArgs e) => 
			{
				var intent = new Intent();
				intent.SetType("image/*");
				intent.SetAction(Intent.ActionGetContent);
				StartActivityForResult(Intent.CreateChooser(intent, "Select Picture"), PickImageId);
			};
		}

		protected override async void OnActivityResult(int requestCode, Result resultCode, Intent data)
		{
			if ((requestCode == PickImageId) && (resultCode == Result.Ok) && (data != null))
			{
				var path = GetPathToImage(data.Data);

				await AddAlbumItem (path);

				await RefreshAlbumItemsGrid ();
			}
		}

		private string GetPathToImage(Android.Net.Uri uri)
		{
			string path = null;
			// The projection contains the columns we want to return in our query.
			string[] projection = new[] { Android.Provider.MediaStore.Images.Media.InterfaceConsts.Data };
			using (ICursor cursor = ManagedQuery(uri, projection, null, null, null))
			{
				if (cursor != null)
				{
					int columnIndex = cursor.GetColumnIndexOrThrow(Android.Provider.MediaStore.Images.Media.InterfaceConsts.Data);
					cursor.MoveToFirst();
					path = cursor.GetString(columnIndex);
				}
			}
			return path;
		}

		private async Task AddAlbumItem(string path)
		{
			using (var streamReader = new StreamReader (path)) {
				// Check stream for picture types other than JPEG
				var picture = await BuddySDK.Buddy.Photos.AddAsync ("", streamReader.BaseStream, "image/jpeg", new BuddyGeoLocation ());

				await AlbumsActivity.SelectedAlbum.AddAsync (picture.ID, "", new BuddyGeoLocation ());
			}
		}

		private async Task RefreshAlbumItemsGrid()
		{
			var albumItems = await AlbumsActivity.SelectedAlbum.Items.FindAsync ();

			var gridview = FindViewById<GridView> (Resource.Id.albumItemsGridView);
			gridview.Adapter = new AlbumItemsAdapter (this, albumItems.ToList());
		}
	}
}