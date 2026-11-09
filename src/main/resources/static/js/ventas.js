// =====================================================================
// VENTANA: NUEVA VENTA (ventas.html)
// Carrito en el navegador; la validacion de stock la hace el servidor.
// =====================================================================

let productos = [];
let carrito = [];   // [{productoId, nombre, precio, cantidad}]

async function cargarClientes() {
  const clientes = await api('GET', '/api/clientes');
  $('cliente').innerHTML = clientes.map(c =>
    `<option value="${c.id}">${c.nombre} (${c.identificacion})</option>`).join('');
}

async function cargarProductos() {
  productos = await api('GET', '/api/productos');
  const seleccionado = $('producto').value;
  $('producto').innerHTML = opcionesProductos(productos);
  if (seleccionado) $('producto').value = seleccionado;
}

function agregarItem() {
  const id = Number($('producto').value);
  const cantidad = Number($('cantidad').value);
  const p = productos.find(x => x.id === id);
  if (!p) return;
  if (!Number.isInteger(cantidad) || cantidad <= 0) {
    mensaje('msgVenta', 'La cantidad debe ser un número entero mayor a cero', false);
    return;
  }
  const existente = carrito.find(i => i.productoId === id);
  if (existente) existente.cantidad += cantidad;
  else carrito.push({ productoId: id, nombre: p.nombre, precio: p.precio, cantidad: cantidad });
  ocultarMensaje('msgVenta');
  pintarCarrito();
}

function quitarItem(i) { carrito.splice(i, 1); pintarCarrito(); }

function pintarCarrito() {
  $('carrito').innerHTML = carrito.length === 0
    ? '<tr><td colspan="5" class="vacio">Sin productos</td></tr>'
    : carrito.map((i, idx) => `
      <tr>
        <td>${i.nombre}</td>
        <td class="num">${i.cantidad}</td>
        <td class="num">$${dinero(i.precio)}</td>
        <td class="num">$${dinero(i.precio * i.cantidad)}</td>
        <td><button class="sec mini" onclick="quitarItem(${idx})">✕</button></td>
      </tr>`).join('');
  $('total').textContent = dinero(carrito.reduce((s, i) => s + i.precio * i.cantidad, 0));
}

async function registrarVenta() {
  if (carrito.length === 0) { mensaje('msgVenta', 'Agregue al menos un producto', false); return; }
  $('btnVender').disabled = true;
  try {
    const venta = await api('POST', '/api/ventas', {
      clienteId: Number($('cliente').value),
      vendedor: $('vendedor').value,
      items: carrito.map(i => ({ productoId: i.productoId, cantidad: i.cantidad }))
    });
    mensaje('msgVenta', `Venta #${venta.id} registrada. Total $${dinero(venta.total)}`, true);
    carrito = [];
    pintarCarrito();
  } catch (e) {
    // La validacion REAL esta en el servidor: aqui solo mostramos su respuesta
    mensaje('msgVenta', e.message, false);
  } finally {
    $('btnVender').disabled = false;
    await cargarProductos();   // el stock mostrado se actualiza
  }
}

// Inicio de la ventana
pintarCarrito();
cargarClientes();
cargarProductos();
