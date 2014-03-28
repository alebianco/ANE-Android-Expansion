package eu.alebianco.air.extensions.expansion.events
{
	import eu.alebianco.air.extensions.expansion.enum.DownloaderClientState;
	import eu.alebianco.air.extensions.expansion.vo.DownloadState;
	
	import flash.events.Event;
	
	public class DownloadStatusChangeEvent extends Event
	{
		public static const STATUS_CHANGE:String = "expansion_download_status_change";
		
		private var _data:DownloadState;
		
		public function DownloadStatusChangeEvent(data:DownloadState, bubbles:Boolean = false, cancelable:Boolean = false)
		{
			super(STATUS_CHANGE, bubbles, cancelable);
			_data = data;
		}

		public function get state():DownloaderClientState
		{
			return _data.state;
		}
		
		public function get message():String
		{
			return _data.message;
		}


		override public function clone():Event
		{
			return new DownloadStatusChangeEvent(_data, bubbles, cancelable);
		}
		override public function toString():String
		{
			return formatToString("DownloadStatusChangeEvent", "type", "state", "message", "bubbles", "cancelable", "eventPhase");
		}
	}
}