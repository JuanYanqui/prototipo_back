// =====================================================================
// VENTANA: INVENTARIO (inventario.html)
// Lista de productos y edicion con bloqueo optimista (columna version).
// =====================================================================

let productos = [];

async function cargarProductos() {
  productos = await api('GET', '/api/productos');
  $('inventario').innerHTML = productos.map(p => `
    <tr>
      <td>${p.nombre}</td>
      <td class="num">$${dinero(p.precio)}</td>
      <td class="num ${p.stock === 0 ? 'agotado' : ''}">${p.stock === 0 ? 'Agotado' : p.stock}</td>
      <td class="num">${p.version}</td>
      <td><button class="sec mini" onclick="abrirEditor(${p.id})">Editar</button></td>
    </tr>`).join('');
}

function abrirEditor(id) {
  const p = productos.find(x => x.id === id);
  $('edId').value = p.id;
  $('edNombre').value = p.nombre;
  $('edPrecio').value = p.precio;
  $('edStock').value = p.stock;
  $('edVersion').textContent = p.version;   // version que el usuario "vio"
  ocultarMensaje('msgEditor');
  $('editor').style.display = 'block';
}

function cerrarEditor() { $('editor').style.display = 'none'; }

async function guardarProducto() {
  try {
    const p = await api('PUT', '/api/productos/' + $('edId').value, {
      nombre: $('edNombre').value,
      precio: Number($('edPrecio').value),
      stock: Number($('edStock').value),
      version: Number($('edVersion').textContent)
    });
    $('edVersion').textContent = p.version;
    mensaje('msgEditor', 'Producto guardado (nueva versión ' + p.version + ')', true);
  } catch (e) {
    // 409: otro empleado (o una venta) cambio el producto antes
    mensaje('msgEditor', e.message, false);
  }
  await cargarProductos();
}

// Inicio de la ventana
cargarProductos();
