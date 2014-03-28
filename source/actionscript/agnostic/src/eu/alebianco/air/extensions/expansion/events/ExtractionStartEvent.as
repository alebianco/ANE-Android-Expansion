package eu.alebianco.air.extensions.expansion.events
{
	import flash.events.Event;
	
	public class ExtractionStartEvent extends Event
	{
		public static const START:String = "expansion_extraction_start";
		
		public function ExtractionStartEvent(bubbles:Boolean = false, cancelable:Boolean = false)
		{
			super(START, bubbles, cancelable);
		}
		override public function clone():Event
		{
			return new ExtractionStartEvent(bubbles, cancelable);
		}
		override public function toString():String
		{
			return formatToString("ExtractionStartEvent", "type", "bubbles", "cancelable", "eventPhase");
		}
	}
}