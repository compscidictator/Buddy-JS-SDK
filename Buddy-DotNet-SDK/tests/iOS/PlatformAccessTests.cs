using System;
using NUnit.Framework;
using BuddySDK;

namespace iOS
{
	[TestFixture]
	public class PlatformAccessTests
	{
		[Test]
		public void ConnectionType()
		{
			Assert.AreEqual (PlatformAccess.NetworkConnectionType.WiFi, PlatformAccess.Current.ConnectionType);
		}
	}
}