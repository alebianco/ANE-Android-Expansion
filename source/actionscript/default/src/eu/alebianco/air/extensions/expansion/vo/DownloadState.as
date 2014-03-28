package eu.alebianco.air.extensions.expansion.vo
{
	import eu.alebianco.air.extensions.expansion.enum.DownloaderClientState;

	public class DownloadState
	{
		private var _message:String;
		private var _state:DownloaderClientState;
		
		public function DownloadState(state:int, message:String)
		{
			super();
			
			_state = DownloaderClientState.getConstants()[state];
			_message = message;
		}

		public function get state():DownloaderClientState
		{
			return _state;
		}

		public function get message():String
		{
			return _message;
		}

	}
}