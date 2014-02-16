using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace CreateLoginUserLibrary
{		
	public class ActivtyBase : Activity
	{
		protected void StartHomeActivity()
		{
			var mainIntent = new Intent("createloginuserlibrary.intent.action.MAIN");

			mainIntent.AddCategory(Intent.CategoryDefault);

			mainIntent.AddFlags(ActivityFlags.NewTask);

			StartActivity (mainIntent);
		}
	}
}