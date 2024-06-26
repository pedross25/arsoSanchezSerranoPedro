using MongoDB.Driver;
using Repositorio;
using System.Collections.Generic;
using System.Linq;
using Usuarios.Modelo;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Usuarios.Repositorio
{
    public class RepositorioMongoDB<T, K> : Repositorio<T, K>
    {
        protected readonly IMongoCollection<T> _collection;

        public RepositorioMongoDB(string collectionName, string connectionString, string databaseName)
        {
            // var database = new MongoClient("mongodb+srv://arso:arso@cluster0.2qwq1ei.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0").GetDatabase("arso");
            // _collection = database.GetCollection<T>(collectionName);
            var database = new MongoClient(connectionString).GetDatabase(databaseName);
            _collection = database.GetCollection<T>(collectionName);
        }

        public K Add(T entity)
        {
            _collection.InsertOne(entity);
            return (K)(entity as dynamic).Id;
        }

        public void Delete(T entity)
        {
            _collection.DeleteOne(Builders<T>.Filter.Eq("Id", (entity as dynamic).Id));
        }

        public List<T> GetAll()
        {
            return _collection.Find(_ => true).ToList();
        }

        public T GetById(K id)
        {
            return _collection.Find(Builders<T>.Filter.Eq("Id", id)).FirstOrDefault();
        }

        public List<K> GetIds()
        {
            var allEntities = _collection.Find(_ => true).ToList();
            return allEntities.Select(e => (K)(e as dynamic).Id).ToList();
        }

        public void Update(T entity)
        {
            _collection.ReplaceOne(Builders<T>.Filter.Eq("Id", (entity as dynamic).Id), entity);
        }
    }

    public class CodigoActivacionRepository : RepositorioMongoDB<CodigoActivacion, string>, ICodigoActivacionRepository
    {
        public CodigoActivacionRepository(string collectionName, string connectionString, string databaseName) : base(collectionName, connectionString, databaseName)
        {
        }

        public CodigoActivacion ObtenerPorCodigoActivacion(string codigoActivacion)
        {
            var filter = Builders<CodigoActivacion>.Filter.Eq(ca => ca.Codigo, codigoActivacion);
            return _collection.Find(filter).FirstOrDefault();
        }
    }
}

