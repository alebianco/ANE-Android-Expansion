package eu.alebianco.air.extensions.expansion
{
	import eu.alebianco.air.extensions.expansion.enum.ExpansionType;
	import eu.alebianco.core.IDisposable;
	
	import flash.utils.Dictionary;
	
	internal class ExpansionMap implements IDisposable
	{
		private var map:Dictionary;
		
		public function ExpansionMap()
		{
			map = new Dictionary();
		}

		public function dispose():void
		{
			map = null;
		}
		
		public function get expansions():Array
		{
			var file:ExpansionFile;
			var list:Array = [];
			
			if (!has(ExpansionType.MAIN)) return list; 
			
			file = map[ExpansionType.MAIN] as ExpansionFile;
			var main:Object = {main:true, version:file.version, size:file.size};
			list.push(main);
			
			if (!has(ExpansionType.PATCH)) return list;
			
			file = map[ExpansionType.PATCH] as ExpansionFile;
			var patch:Object = {main:true, version:file.version, size:file.size};
			list.push(patch);
			
			return list;
		}
		
		public function has(type:ExpansionType):Boolean
		{
			return (type in map);
		}
		
		public function add(file:ExpansionFile):void
		{
			if (file == null) return;
			if (has(file.type)) 
			{
				throw new ArgumentError("Can't require the same type of expansion ("+file.type.name+") twice.");
			}
			
			if (file.type == ExpansionType.PATCH && !has(ExpansionType.MAIN))
			{
				throw new ArgumentError("Can't add a 'patch' expansion without a 'main' expansion.");
			}
			
			map[file.type] = file;
		}
		public function remove(file:ExpansionFile):void
		{
			if (file.type == ExpansionType.MAIN)
			{
				removeAll();
			}
			else if (file != null && map[file.type] == file)
			{
				delete map[file.type];
			}
		}
		public function removeByType(type:ExpansionType):void
		{
			if (type == ExpansionType.MAIN)
			{
				removeAll();
			}
			else
			{
				delete map[type];
			}
		}
		public function removeAll():void
		{
			map = new Dictionary();
		}
	}
}