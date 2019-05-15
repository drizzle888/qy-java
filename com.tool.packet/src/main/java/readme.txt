目标: 
	该工具通过xml模板定义文件生成java和csharp类文件，制定网络传输对象，再通过反射机制，可以序列化与反序列化对象，使得传输定义简单易懂

文件目录:
	模板文件: src/main/resources/
	java文件: src/main/generates/java/
	csharp文件: src/main/generates/csharp/

类属性:
	name: 生成class类名
	parent: 生成java和csharp类的父类名
	package: 生成java类的package，与csharp无关
	namespace: 生成csharp的名称空间，与java无关
	
类引用:
	import: java引用类包名，与csharp无关
	using: csharp引用类包名，与java无关

字段类型对应关系:
	type			generic Class		element Class		java				csharp
	----------------------------------------------------------------------------------
	boolean			null				null				boolean				bool
	byte			null				null				byte				byte
	short			null				null				short				short
	int				null				null				int					int
	long			null				null				long				long
	float			null				null				float				float
	String			null				null				String				string
	PackageBase		null				null				PackageBase			PackageBase
	List			Class				null				List<Class>			List<Class>
	Array			null				Class				Class[]				Class[]

注意: 
	1. ClassBase包括: bool, byte, short, int, long, float, String
	2. PackageBase是以PackageBase为父类的衍生类型
	3. Class可以是ClassBase类型或PackageBase类型