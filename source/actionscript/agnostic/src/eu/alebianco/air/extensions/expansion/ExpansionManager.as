
package eu.alebianco.air.extensions.expansion
{
	import eu.alebianco.air.extensions.expansion.enum.DownloaderClientState;
	import eu.alebianco.air.extensions.expansion.enum.ExpansionType;
	import eu.alebianco.air.extensions.expansion.enum.StatusLevel;
	import eu.alebianco.air.extensions.expansion.events.DownloadCompleteEvent;
	import eu.alebianco.air.extensions.expansion.events.DownloadProgressEvent;
	import eu.alebianco.air.extensions.expansion.events.DownloadStatusChangeEvent;
	import eu.alebianco.air.extensions.expansion.events.ExtractionCompleteEvent;
	import eu.alebianco.air.extensions.expansion.events.ExtractionErrorEvent;
	import eu.alebianco.air.extensions.expansion.events.ExtractionStartEvent;
	import eu.alebianco.air.extensions.expansion.ns.downloadStatusLevel;
	import eu.alebianco.air.extensions.expansion.ns.extractorStatusLevel;
	import eu.alebianco.air.extensions.expansion.ns.logStatusLevel;
	import eu.alebianco.air.extensions.expansion.vo.DownloadState;
	import eu.alebianco.air.extensions.expansion.vo.ProgressInfo;
	import eu.alebianco.core.IDisposable;
	
	import flash.errors.IllegalOperationError;
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	import flash.utils.getQualifiedClassName;
	
	import mx.logging.ILogger;
	import mx.logging.Log;
	
	[Event(name="expansion_download_progress", type="eu.alebianco.air.extensions.expansion.events.DownloadProgressEvent")]
	[Event(name="expansion_download_complete", type="eu.alebianco.air.extensions.expansion.events.DownloadCompleteEvent")]
	[Event(name="expansion_download_status_change", type="eu.alebianco.air.extensions.expansion.events.DownloadStatusChangeEvent")]
	[Event(name="expansion_extraction_start", type="eu.alebianco.air.extensions.expansion.events.ExtractionStartEvent")]
	[Event(name="expansion_extraction_complete", type="eu.alebianco.air.extensions.expansion.events.ExtractionCompleteEvent")]
	[Event(name="expansion_extraction_error", type="eu.alebianco.air.extensions.expansion.events.ExtractionErrorEvent")]
	public final class ExpansionManager extends EventDispatcher implements IDisposable
	{
		private static const EXTENSION_ID:String = "eu.alebianco.air.extensions.expansion";
		
		private static const STATUS_NONE:int = 0;
		private static const STATUS_SECURITY:int = 1;
		private static const STATUS_EXPANSIONS:int = 2;
		private static const STATUS_READY:int = STATUS_SECURITY | STATUS_EXPANSIONS;
		
		private var logger:ILogger;
		
		private static var instance:ExpansionManager;
		private static var canBuild:Boolean;
		
		private var context:ExtensionContext;
		private var requiresMap:ExpansionMap;
		
		private var status:int = STATUS_NONE;
		
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
			var supported:Boolean;
			
			try
			{
				getInstance();
				supported = instance.context.call("isSupported");
			}
			catch(error:Error)
			{
				supported = false;
			}
			
			return supported;
		}
		
		public function ExpansionManager()
		{
			if (!canBuild)
			{
				throw new Error("Can't instantiate a Singleton class, use getInstance() to get a reference.");
			}
			
			if (!context) 
			{
				context = ExtensionContext.createExtensionContext(EXTENSION_ID, null);
				context.addEventListener(StatusEvent.STATUS, statusHandler);
			}
			
			status = STATUS_NONE;
			requiresMap = new ExpansionMap();
			
			var className:String = getQualifiedClassName(this).replace("::", ".");
			logger = Log.getLogger(className);
		}
		
		public function dispose():void {
			
			status = STATUS_NONE;
			
			context.removeEventListener(StatusEvent.STATUS, statusHandler);
			context = null;
			instance = null;
			
			requiresMap.dispose();
			requiresMap = null;
		}
		
		private function statusHandler(event:StatusEvent):void {
			
			var level:StatusLevel = StatusLevel.parseConstant(event.level);
			if (level)
			{
				var ns:Namespace = level.ns;
				ns::processStatusEvent(level.name, event.code);
			}
		}
		
		logStatusLevel function processStatusEvent(level:String, code:String):void
		{
			switch(level.toUpperCase())
			{
				case "INFO":
					logger.info(code);
					break;
				case "DEBUG":
					logger.debug(code);
					break;
				case "WARN":
					logger.warn(code);
					break;
				case "ERROR":
					logger.error(code);
					break;
				case "FATAL":
					logger.fatal(code);
					break;
				default:
					logger.debug("Level ("+level+") not found.");
					break;
			}
		}
		downloadStatusLevel function processStatusEvent(level:String, code:String):void
		{
			if (code == "progress")
			{
				var progress:ProgressInfo = context.call("getDownloadProgress") as ProgressInfo;
				dispatchEvent(new DownloadProgressEvent(progress));
			}
			else if (code == "complete")
			{
				dispatchEvent(new DownloadCompleteEvent());
			}
			else if (code == "status_change")
			{
				var current:DownloadState = context.call("getDownloadState") as DownloadState;
				
				if (DownloaderClientState.COMPLETED == current.state)
				{
					dispatchEvent(new DownloadCompleteEvent());
				}
				else
				{
					dispatchEvent(new DownloadStatusChangeEvent(current));
				}
			}
		}
		extractorStatusLevel function processStatusEvent(level:String, code:String):void
		{
			if (code == "begin")
			{
				dispatchEvent(new ExtractionStartEvent());
			}
			else if (code == "complete")
			{
				dispatchEvent(new ExtractionCompleteEvent());	
			}
			else if (code == "error")
			{
				dispatchEvent(new ExtractionErrorEvent());
			}
		}
		
		public function setupMarketSecurity(key:String, salt:Vector.<int>):void
		{
			if (status & STATUS_SECURITY != 0)
			{
				throw new IllegalOperationError("Operation already setup.");
			}
			
			var result:Boolean = context.call("setMarketSecurity", key, salt);
			if (result != true)
			{
				throw new Error("Unable to setup security parameters, application can't continue.");
			}
			else
			{
				status |= STATUS_SECURITY;
			}
		}
		
		public function addRequiredExpansion(file:ExpansionFile):void
		{
			requiresMap.add(file);
			checkFileRequests();
		}
		public function removeRequiredExpansion(file:ExpansionFile):void
		{
			requiresMap.remove(file);
			checkFileRequests();
		}
		public function removeRequiredExpansionByType(type:ExpansionType):void
		{
			requiresMap.removeByType(type);
			checkFileRequests();
		}
		public function removeAllRequiredExpansions():void
		{
			requiresMap.removeAll();
			checkFileRequests();
		}
		private function checkFileRequests():void
		{
			if (requiresMap.has(ExpansionType.MAIN))
			{
				status |= STATUS_EXPANSIONS;
			}
			else
			{
				status &= ~STATUS_EXPANSIONS;
			}
		}
		
		public function hasExpansionFiles():Boolean
		{
			checkStatus();
			return context.call("hasExpansionFiles", requiresMap.expansions);
		}
		
		public function download():void
		{
			checkStatus();
			context.call("downloadExpansions");
		}
		
		public function showWiFiSettings():void
		{
			context.call("showWiFiSettings");
		}
		
		public function authorizeMobileDownload():void
		{
			context.call("authorizeMobileDownload");
		}
		
		public function pause():void
		{
			context.call("pauseDownload");
		}
		public function resume():void
		{
			context.call("resumeDownload");
		}
		public function cancel():void
		{
			context.call("cancelDownload");
		}
		
		public function unzip(path:String, overwrite:Boolean = false):void
		{
			context.call("unzipExpansionContent", path, requiresMap.expansions, overwrite);
		}
		
		private function checkStatus():void
		{
			if ((status & STATUS_READY) != STATUS_READY)
			{
				var message:String = "";
				
				switch(true)
				{
					case (status & STATUS_SECURITY) == 0:
					{
						message = "You havent configured the market security. Plase call setupMarketSecurity() to setup your data.";
						break;
					}
						
					case (status & STATUS_EXPANSIONS) == 0:
					{
						message = "You haven't required any expansion file, so why bothering to use this extension at all?";
						break;
					}
				}
				
				throw new IllegalOperationError(message);
			}
		}
	}
}
