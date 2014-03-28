package eu.alebianco.air.extensions.expansion
{
	import eu.alebianco.core.IDisposable;
	import eu.alebianco.air.extensions.expansion.enum.ExpansionType;
	
	import flash.events.EventDispatcher;
	
	[Event(name="expansion_download_progress", type="eu.alebianco.air.extensions.expansion.events.DownloadProgressEvent")]
	[Event(name="expansion_download_complete", type="eu.alebianco.air.extensions.expansion.events.DownloadCompleteEvent")]
	[Event(name="expansion_download_status_change", type="eu.alebianco.air.extensions.expansion.events.DownloadStatusChangeEvent")]
	[Event(name="expansion_extraction_start", type="eu.alebianco.air.extensions.expansion.events.ExtractionStartEvent")]
	[Event(name="expansion_extraction_complete", type="eu.alebianco.air.extensions.expansion.events.ExtractionCompleteEvent")]
	[Event(name="expansion_extraction_error", type="eu.alebianco.air.extensions.expansion.events.ExtractionErrorEvent")]
	public final class ExpansionManager extends EventDispatcher implements IDisposable
	{
		private static var instance:ExpansionManager;
		private static var canBuild:Boolean;
		
		public static function getInstance():ExpansionManager
		{
			if (!instance)
			{
				canBuild = true;
				instance = new ExpansionManager();
				canBuild = false;
			}
			return instance;
		}
		
		public static function isSupported():Boolean
		{
			return false;
		}
		
		public function ExpansionManager()
		{
			if (!canBuild)
			{
				throw new Error("Can't instantiate a Singleton class, use getInstance() to get a reference.");
			}
		}
		
		public function dispose():void {
			
			instance = null;
		}
		
		public function setupMarketSecurity(key:String, salt:Vector.<int>):void
		{
		}
		
		public function addRequiredExpansion(file:ExpansionFile):void
		{
		}
		public function removeRequiredExpansion(file:ExpansionFile):void
		{
		}
		public function removeRequiredExpansionByType(type:ExpansionType):void
		{
		}
		public function removeAllRequiredExpansions():void
		{
		}
		
		public function hasExpansionFiles():Boolean
		{
			return false;
		}
		
		public function download():void
		{
		}
		
		public function showWiFiSettings():void
		{
		}
		public function authorizeMobileDownload():void
		{
		}
		
		
		public function pause():void
		{
		}
		public function resume():void
		{
		}
		public function cancel():void
		{
		}
		
		public function unzip(path:String, overwrite:Boolean = false):void
		{
		}
	}
}