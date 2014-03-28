package eu.alebianco.air.extensions.expansion.enum
{
	import eu.alebianco.core.Enum;
	
	public class ExpansionType extends Enum
	{
		{ initEnum(ExpansionType); }
		
		// Constants.
		
		public static const MAIN:ExpansionType = new ExpansionType();
		public static const PATCH:ExpansionType = new ExpansionType();
		
		// Constant query.
		
		public static function getConstants():Vector.<ExpansionType>
		{
			return Vector.<ExpansionType>(Enum.getConstants(ExpansionType));
		}
		public static function parseConstant(constantName:String, caseSensitive:Boolean = false):ExpansionType
		{
			return ExpansionType(Enum.parseConstant(ExpansionType, constantName, caseSensitive));
		}
		
		// Properties.
		
		// Constructor.
		
		public function ExpansionType()
		{
			super();
		}
		
		// Accessors.
		
		// Utilities.
		override public function toString():String
		{
			return "[Expansion Type (name: " + name + ")]";
		}
	}
}