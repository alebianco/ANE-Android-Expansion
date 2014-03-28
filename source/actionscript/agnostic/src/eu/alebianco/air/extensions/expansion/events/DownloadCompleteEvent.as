package eu.alebianco.air.extensions.expansion.events
{
	import flash.events.Event;
	
	public class DownloadCompleteEvent extends Event
	{
		public static const COMPLETE:String = "expansion_download_complete";
		
		public function DownloadCompleteEvent(bubbles:Boolean = false, cancelable:Boolean = false)
		{
			super(COMPLETE, bubbles, cancelable);
		}
		override public function clone():Event
		{
			return new DownloadCompleteEvent(bubbles, cancelable);
		}
		override public function toString():String
		{
			return formatToString("DownloadCompleteEvent", "type", "bubbles", "cancelable", "eventPhase");
		}
	}
}