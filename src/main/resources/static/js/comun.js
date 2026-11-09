// =====================================================================
// FUNCIONES COMUNES - las usan todas las ventanas.
// Aqui esta la comunicacion con la API REST del servidor (Controlador).
// =====================================================================

const $ = id => document.getElementById(id);
const dinero = n => Number(n).toFixed(2);

/** Llama a la API del servidor. Si responde con error, lanza el mensaje que envio el servidor. */
async function api(metodo, url, cuerpo) {
  const resp = await fetch(url, {
    method: metodo,
    headers: { 'Content-Type': 'application/json' },
    body: cuerpo ? JSON.stringify(cuerpo) : undefined
  });
  const datos = await resp.json().catch(() => null);
  if (!resp.ok) throw new Error((datos && datos.mensaje) || ('Error ' + resp.status));
  return datos;
}

/** Muestra un mensaje verde (ok = true) o rojo (ok = false) en el elemento indicado. */
function mensaje(id, texto, ok) {
  const el = $(id);
  el.className = 'msg ' + (ok ? 'ok' : 'error');
  el.textContent = texto;
}

function ocultarMensaje(id) { $(id).className = 'msg'; }

/** Opciones <option> para un combo de productos. */
function opcionesProductos(productos) {
  return productos.map(p =>
    `<option value="${p.id}">${p.nombre} - $${dinero(p.precio)} (stock ${p.stock})</option>`).join('');
}
