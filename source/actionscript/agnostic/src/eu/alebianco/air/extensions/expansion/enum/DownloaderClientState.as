package eu.alebianco.air.extensions.expansion.enum
{
	import eu.alebianco.core.Enum;
	
	public class DownloaderClientState extends Enum
	{
		{ initEnum(DownloaderClientState); }
		
		// Constants.
		public static const NONE:DownloaderClientState = new DownloaderClientState();
		
		public static const IDLE:DownloaderClientState = new DownloaderClientState();
		public static const FETCHING_URL:DownloaderClientState = new DownloaderClientState();
		public static const CONNECTING:DownloaderClientState = new DownloaderClientState();
		public static const DOWNLOADING:DownloaderClientState = new DownloaderClientState();
		public static const COMPLETED:DownloaderClientState = new DownloaderClientState();
		
		public static const PAUSED_NETWORK_UNAVAILABLE:DownloaderClientState = new DownloaderClientState();
		public static const PAUSED_BY_REQUEST:DownloaderClientState = new DownloaderClientState();
		
		public static const PAUSED_WIFI_DISABLED_NEED_CELLULAR_PERMISSION:DownloaderClientState = new DownloaderClientState();
		public static const PAUSED_NEED_CELLULAR_PERMISSION:DownloaderClientState = new DownloaderClientState();
		public static const PAUSED_ROAMING:DownloaderClientState = new DownloaderClientState();
		
		public static const PAUSED_NETWORK_SETUP_FAILURE:DownloaderClientState = new DownloaderClientState();
		public static const PAUSED_SDCARD_UNAVAILABLE:DownloaderClientState = new DownloaderClientState();
		
		public static const FAILED_UNLICENSED:DownloaderClientState = new DownloaderClientState();
		public static const FAILED_FETCHING_URL:DownloaderClientState = new DownloaderClientState();
		public static const FAILED_SDCARD_FULL:DownloaderClientState = new DownloaderClientState();
		public static const FAILED_CANCELED:DownloaderClientState = new DownloaderClientState();
		
		public static const FAILED:DownloaderClientState = new DownloaderClientState();
		
		// Constant query.
		
		public static function getConstants():Vector.<DownloaderClientState>
		{
			return Vector.<DownloaderClientState>(Enum.getConstants(DownloaderClientState));
		}
		public static function parseConstant(constantName:String, caseSensitive:Boolean = false):DownloaderClientState
		{
			return DownloaderClientState(Enum.parseConstant(DownloaderClientState, constantName, caseSensitive));
		}
		
		// Properties.
		
		// Constructor.
		
		public function DownloaderClientState()
		{
			super();
		}
		
		// Accessors.
		
		// Utilities.
		override public function toString():String
		{
			return "[Downloader Client State (name: " + name + ")]";
		}
	}
}