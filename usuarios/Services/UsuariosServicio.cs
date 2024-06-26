using Usuarios.Modelo;
using Repositorio;
using Usuarios.Repositorio;

namespace Usuarios.Servicio {

    public interface IServicioUsuarios
    {
        string SolicitarCodigoActivacion(string usuarioId);
        string AltaUsuario(Usuario usuario, string codigoActivacion);
        void BajaUsuario(string id);
        Dictionary<string, string>? VerificarCredenciales(string correoElectronico, string contrasena);
        Dictionary<string, string>? VerificarUsuarioOAuth2(string oauth2Id);
        List<Usuario> ListadoUsuarios();
    }

    public class ServicioUsuarios : IServicioUsuarios {
        private Repositorio<Usuario, string> _repositorioUsuarios;
        private ICodigoActivacionRepository _repositorioCodigos;

        public ServicioUsuarios(Repositorio<Usuario, string> repositorioUsuarios, ICodigoActivacionRepository repositorioCodigos)
        {
            this._repositorioUsuarios = repositorioUsuarios;
            this._repositorioCodigos = repositorioCodigos;
        }

        public string SolicitarCodigoActivacion(string usuarioId)
        {
            var codigo = new CodigoActivacion
            {
                UsuarioId = usuarioId,
                Codigo = Guid.NewGuid().ToString().Substring(0, 6),
                FechaExpiracion = DateTime.UtcNow.AddHours(1)
            };
            return _repositorioCodigos.Add(codigo);
        }

         public string AltaUsuario(Usuario usuario, string codigoActivacion)
        {
            var codigo = _repositorioCodigos.ObtenerPorCodigoActivacion(codigoActivacion);
            if (codigo == null || codigo.FechaExpiracion < DateTime.UtcNow)
                throw new Exception("Código de activación inválido o expirado.");

            return _repositorioUsuarios.Add(usuario);
        }   

        public void BajaUsuario(string id)
        {
            Usuario usuario = _repositorioUsuarios.GetById(id);
            _repositorioUsuarios.Delete(usuario);
        }

        public Dictionary<string, string>? VerificarCredenciales(string User, string Password)
        {
            var usuarios = _repositorioUsuarios.GetAll();
            var usuario = usuarios.FirstOrDefault(u => u.Nombre == User && u.Contrasena == Password);

            if (usuario != null)
            {
                return GenerarClaims(usuario);
            }

            return null;
        }

        public Dictionary<string, string>? VerificarUsuarioOAuth2(string oauth2Id)
        {
            Console.WriteLine(oauth2Id);
            var usuarios = _repositorioUsuarios.GetAll();
            var usuario = usuarios.FirstOrDefault(u => u.OAuth2Id == oauth2Id);

            if (usuario != null)
            {
                return GenerarClaims(usuario);
            }

            return null;
        }

        public List<Usuario> ListadoUsuarios()
        {
            return _repositorioUsuarios.GetAll();
        }

        private Dictionary<string, string> GenerarClaims(Usuario Usuario)
        {
            return new Dictionary<string, string>
            {
                { "userId", Usuario.Id },
                { "name", Usuario.Nombre },
                { "rol", Usuario.Rol }
            };
        }
    }
}