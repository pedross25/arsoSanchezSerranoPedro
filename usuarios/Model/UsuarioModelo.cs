
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace Usuarios.Modelo {
    public class Usuario
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        [BsonIgnoreIfDefault]
        public string Id { get; set; }

        [BsonRequired]
        public string Nombre { get; set; }
        public string CorreoElectronico { get; set; }
        public string Telefono { get; set; }
        public string DireccionPostal { get; set; }
        public string Contrasena { get; set; }
        public string OAuth2Id { get; set; }
        public string Rol { get; set; }
    }

    public class CodigoActivacion
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string UsuarioId { get; set; }
        public string Codigo { get; set; }
        public DateTime FechaExpiracion { get; set; }
    }
}