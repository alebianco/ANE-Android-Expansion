package eu.alebianco.air.extensions.expansion.events
{
	import eu.alebianco.air.extensions.expansion.vo.ProgressInfo;
	
	import flash.events.Event;
	
	public class DownloadProgressEvent extends Event
	{
		public static const PROGRESS:String = "expansion_download_progress";
		
		public var info:ProgressInfo;
		
		public function DownloadProgressEvent(info:ProgressInfo, bubbles:Boolean = false, cancelable:Boolean = false)
		{
			super(PROGRESS, bubbles, cancelable);
			this.info = info;
		}
		override public function clone():Event
		{
			return new DownloadProgressEvent(info, bubbles, cancelable);
		}
		override public function toString():String
		{
			return formatToString("DownloadProgressEvent", "type", "info", "bubbles", "cancelable", "eventPhase");
		}
	}
}