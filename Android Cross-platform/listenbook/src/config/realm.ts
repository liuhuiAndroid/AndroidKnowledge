import Realm from 'realm';

class Show {
  static schema = {
    name: 'Show',
    primaryKey: 'id',
    properties: {
      id: 'string',
      title: 'string',
      thumbnailUrl: 'string',
      playSeconds: {type: 'double', default: 0},
      duration: {type: 'double', default: 0},
    },
  };
}

// 第一个架构将会被更新到现有的架构版本
// 因为第一个架构位于数组顶部
// var nextSchemaIndex = Realm.schemaVersion(Realm.defaultPath);

// console.log('-------realm', nextSchemaIndex)

// var schemas = [
//     { schema: [Show], schemaVersion: 0 },
//     { schema: [Show], schemaVersion: 1 },
// ]

// while (nextSchemaIndex < schemas.length) {
//     var migratedRealm = new Realm(schemas[nextSchemaIndex++]);
//     // migratedRealm.clear();
//     migratedRealm.close();
// }

// 使用最新的架构打开 Realm 数据库
// var realm = new Realm(schemas[schemas.length - 1]);

const realm = new Realm({schema: [Show], schemaVersion: 1});

export default realm;
