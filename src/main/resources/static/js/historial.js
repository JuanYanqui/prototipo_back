// =====================================================================
// VENTANA: HISTORIAL DE VENTAS (historial.html)
// =====================================================================

async function cargarVentas() {
  const ventas = await api('GET', '/api/ventas');
  $('ventas').innerHTML = ventas.length === 0
    ? '<tr><td colspan="6" class="vacio">Aún no hay ventas</td></tr>'
    : ventas.map(v => `
      <tr>
        <td>${v.id}</td>
        <td>${new Date(v.fecha).toLocaleString()}</td>
        <td>${v.cliente}</td>
        <td>${v.vendedor}</td>
        <td>${v.detalles.map(d => d.cantidad + ' x ' + d.producto).join(', ')}</td>
        <td class="num">$${dinero(v.total)}</td>
      </tr>`).join('');
}

// Inicio de la ventana
cargarVentas();
