// =====================================================================
// VENTANA: DEMO DE CONCURRENCIA (concurrencia.html)
// Dispara N ventas al mismo tiempo del mismo producto (N vendedores).
// =====================================================================

let productos = [];
let clienteId = null;

async function cargarProductos() {
  productos = await api('GET', '/api/productos');
  const seleccionado = $('simProducto').value;
  $('simProducto').innerHTML = opcionesProductos(productos);
  if (seleccionado) $('simProducto').value = seleccionado;
}

async function simular() {
  const id = Number($('simProducto').value);
  const n = Number($('simN').value);
  if (!Number.isInteger(n) || n < 2) { mensaje('msgSim', 'Indique al menos 2 vendedores', false); return; }
  ocultarMensaje('msgSim');
  $('btnSim').disabled = true;

  await cargarProductos();
  const antes = productos.find(p => p.id === id).stock;

  // Se disparan N peticiones al mismo tiempo, como N vendedores distintos
  const peticiones = Array.from({ length: n }, (_, i) =>
    api('POST', '/api/ventas', {
      clienteId: clienteId,
      vendedor: 'Vendedor simultáneo ' + (i + 1),
      items: [{ productoId: id, cantidad: 1 }]
    }).then(() => true, () => false));

  const resultados = await Promise.all(peticiones);
  await cargarProductos();

  $('resSim').style.display = 'flex';
  $('simAntes').textContent = antes;
  $('simOk').textContent = resultados.filter(r => r).length;
  $('simNo').textContent = resultados.filter(r => !r).length;
  $('simDespues').textContent = productos.find(p => p.id === id).stock;
  $('btnSim').disabled = false;
}

// Inicio de la ventana
(async () => {
  const clientes = await api('GET', '/api/clientes');
  clienteId = clientes.length ? clientes[0].id : null;
  await cargarProductos();
  // Preseleccionar el producto de "ultima unidad" para la demo
  const ultima = productos.find(p => p.stock === 1);
  if (ultima) $('simProducto').value = ultima.id;
})();
