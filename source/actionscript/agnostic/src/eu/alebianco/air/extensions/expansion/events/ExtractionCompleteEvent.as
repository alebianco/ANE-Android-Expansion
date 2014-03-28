package eu.alebianco.air.extensions.expansion.events
{
	import flash.events.Event;
	
	public class ExtractionCompleteEvent extends Event
	{
		public static const COMPLETE:String = "expansion_extraction_complete";
		
		public function ExtractionCompleteEvent(bubbles:Boolean = false, cancelable:Boolean = false)
		{
			super(COMPLETE, bubbles, cancelable);
		}
		override public function clone():Event
		{
			return new ExtractionCompleteEvent(bubbles, cancelable);
		}
		override public function toString():String
		{
			return formatToString("ExtractionCompleteEvent", "type", "bubbles", "cancelable", "eventPhase");
		}
	}
}