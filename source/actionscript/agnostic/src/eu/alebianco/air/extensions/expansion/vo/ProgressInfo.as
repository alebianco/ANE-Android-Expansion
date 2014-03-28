package eu.alebianco.air.extensions.expansion.vo
{
	public class ProgressInfo
	{
		public var bytesLoaded:int;
		public var bytesTotal:int;

		public var speed:String;
		public var remaining:String;
		
		public function get progress():Number
		{
			return bytesLoaded/bytesTotal;
		}
	}
}