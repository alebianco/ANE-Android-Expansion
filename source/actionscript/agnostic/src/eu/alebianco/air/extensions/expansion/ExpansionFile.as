package eu.alebianco.air.extensions.expansion
{
	import eu.alebianco.air.extensions.expansion.enum.ExpansionType;

	public class ExpansionFile
	{
		private var _type:ExpansionType;
		private var _version:int;
		private var _size:int;
		
		public function ExpansionFile(type:ExpansionType, version:int, size:int)
		{
			_type = type;
			_version = version;
			_size = size;
		}
		
		public function get type():ExpansionType
		{
			return _type;
		}
		
		public function get version():int
		{
			return _version;
		}
		
		public function get size():int
		{
			return _size;
		}

	}
}