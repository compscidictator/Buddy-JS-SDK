using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

using Android.App;
using Android.Content;
using Android.Graphics;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

using BuddySDK;

namespace AlbumsSample
{
	public class AlbumItemsAdapter : BaseAdapter
	{
		private readonly Context context;
		private	readonly IEnumerable<AlbumItem> albumItems;

		public AlbumItemsAdapter(Context c, IEnumerable<AlbumItem> ai)
		{
			context = c;

			albumItems = ai;
		}

		public override int Count
		{
			get { return albumItems.Count(); }
		}

		public override Java.Lang.Object GetItem(int position)
		{
			return null;
		}

		public override long GetItemId(int position)
		{
			return 0;
		}

		public override View GetView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView;

			if (convertView == null)
			{
				// if it's not recycled, initialize some attributes
				imageView = new ImageView(context);
				imageView.LayoutParameters = new AbsListView.LayoutParams(85, 85);
				imageView.SetScaleType(ImageView.ScaleType.CenterCrop);
				imageView.SetPadding(8, 8, 8, 8);
			}
			else
			{
				imageView = (ImageView) convertView;
			}

			// Cache UI thread synchronization context.
			var uiContext = TaskScheduler.FromCurrentSynchronizationContext ();

			Task.Run (async () => {
				var picture = await Buddy.Photos.FindAsync (albumItems.Skip (position).First ().ItemId);

				var pictureStream = await picture.First ().GetFileAsync ();

				// Use ContinueWith() instead of await here, so decodeTask, including SetImageBitmap(),
				// will be called on the uiContext.
				BitmapFactory.DecodeStreamAsync (pictureStream).ContinueWith((decodeTask) =>
					{
						imageView.SetImageBitmap (decodeTask.Result);
					}, CancellationToken.None, TaskContinuationOptions.DenyChildAttach, uiContext);
			});

			return imageView;
		}
	}
}