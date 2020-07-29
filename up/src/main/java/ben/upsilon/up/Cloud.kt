package ben.upsilon.up

class Cloud(env: String) {

    val core:Core = Core(env)
    val auth:Auth= Auth(core,env)
    val database:Database = Database(core,env)




    class Core(env: String) {

    }

    class Storage{

    }
    class Database(core: Core, env: String) {

    }

    class Auth(core: Core, env: String) {

    }
}