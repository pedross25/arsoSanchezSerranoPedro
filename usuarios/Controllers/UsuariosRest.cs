using Microsoft.AspNetCore.Mvc;
using Usuarios.Servicio;
using Usuarios.Modelo;
using System;
using System.Collections.Generic;

namespace UsuariosAPI.Controllers
{
    [Route("api/usuarios")]
    [ApiController]
    public class UsuariosController : ControllerBase
    {
        private readonly IServicioUsuarios _servicio;

        public UsuariosController(IServicioUsuarios servicio)
        {
            _servicio = servicio;
        }

        // POST api/usuarios/solicitar-codigo-activacion/{usuarioId}
        [HttpPost("solicitar-codigo-activacion/{usuarioId}")]
        public ActionResult<string> SolicitarCodigoActivacion(string usuarioId)
        {
            try
            {
                var codigo = _servicio.SolicitarCodigoActivacion(usuarioId);
                return Ok(codigo);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        // POST api/usuarios/alta
        [HttpPost("alta")]
        public ActionResult<string> AltaUsuario(Usuario usuario, [FromQuery] string codigoActivacion)
        {
            try
            {
                var idNuevoUsuario = _servicio.AltaUsuario(usuario, codigoActivacion);
                return Ok(idNuevoUsuario);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        // DELETE api/usuarios/baja/{id}
        [HttpDelete("baja/{id}")]
        public ActionResult BajaUsuario(string id)
        {
            try
            {
                _servicio.BajaUsuario(id);
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        // POST api/usuarios/verificar-credenciales
        [HttpPost("verificar-credenciales")]
        public ActionResult<Dictionary<string, string>> VerificarCredenciales([FromBody] Credenciales Credenciales)
        {
            try
            {
                var claims = _servicio.VerificarCredenciales(Credenciales.User, Credenciales.Password);
                if (claims != null)
                {
                    return Ok(claims);
                }
                else
                {
                    return NotFound("Credenciales incorrectas");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        // POST api/usuarios/verificar-oauth2
        [HttpPost("verificar-oauth2")]
        public ActionResult<Dictionary<string, string>> VerificarUsuarioOAuth2([FromBody] OAuth2Request oauth2Request)
        {
            Console.WriteLine("oauth2Id");
            try
            {
                var claims = _servicio.VerificarUsuarioOAuth2(oauth2Request.OAuth2Id);
                if (claims != null)
                {
                    return Ok(claims);
                }
                else
                {
                    return NotFound("Identificador OAuth2 no encontrado");
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        // GET api/usuarios
        [HttpGet("listar")]
        public ActionResult<List<Usuario>> ListadoUsuarios()
        {
            try
            {
                var usuarios = _servicio.ListadoUsuarios();
                return Ok(usuarios);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }
    }

    public class Credenciales
    {
        public string User { get; set; }
        public string Password { get; set; }
    }

    public class OAuth2Request
    {
        public string OAuth2Id { get; set; }
    }
}
