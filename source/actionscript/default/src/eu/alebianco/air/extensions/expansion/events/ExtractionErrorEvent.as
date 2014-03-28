package eu.alebianco.air.extensions.expansion.events
{
	import eu.alebianco.air.extensions.expansion.vo.ProgressInfo;
	
	import flash.events.ErrorEvent;
	import flash.events.Event;
	
	public class ExtractionErrorEvent extends ErrorEvent
	{
		public static const ERROR:String = "expansion_extraction_error";
		
		public function ExtractionErrorEvent(bubbles:Boolean = false, cancelable:Boolean = false)
		{
			super(ERROR, bubbles, cancelable);
		}
		override public function clone():Event
		{
			return new ExtractionErrorEvent(bubbles, cancelable);
		}
		override public function toString():String
		{
			return formatToString("ExtractionErrorEvent", "type", "bubbles", "cancelable", "eventPhase");
		}
	}
}