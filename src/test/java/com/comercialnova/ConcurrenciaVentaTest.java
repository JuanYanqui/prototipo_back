package com.comercialnova;

import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

import com.comercialnova.dto.ItemVentaRequest;
import com.comercialnova.dto.VentaRequest;
import com.comercialnova.exception.StockInsuficienteException;
import com.comercialnova.model.Producto;
import com.comercialnova.repository.ClienteRepository;
import com.comercialnova.repository.ProductoRepository;
import com.comercialnova.service.VentaService;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConcurrenciaVentaTest {

    @Autowired VentaService ventaService;
    @Autowired ProductoRepository productoRepository;
    @Autowired ClienteRepository clienteRepository;

    @Test
    @DisplayName("20 vendedores y 1 unidad: solo uno vende, 19 son rechazados")
    void veinteVendedoresUltimaUnidad() throws Exception {
        Resultado r = simular(1, 20);

        assertThat(r.exitos).isEqualTo(1);
        assertThat(r.sinStock).isEqualTo(19);
        assertThat(r.otrosErrores).isZero();
        assertThat(r.stockFinal).isZero();
    }

    @Test
    @DisplayName("20 vendedores y 5 unidades: exactamente 5 ventas, stock nunca negativo")
    void veinteVendedoresCincoUnidades() throws Exception {
        Resultado r = simular(5, 20);

        assertThat(r.exitos).isEqualTo(5);
        assertThat(r.sinStock).isEqualTo(15);
        assertThat(r.otrosErrores).isZero();
        assertThat(r.stockFinal).isZero();
    }

    private Resultado simular(int stockInicial, int vendedores) throws Exception {
        Producto p = productoRepository.saveAndFlush(
                new Producto("Producto concurrencia", new BigDecimal("100.00"), stockInicial));
        Long clienteId = clienteRepository.findAll().get(0).getId();

        AtomicInteger exitos = new AtomicInteger();
        AtomicInteger sinStock = new AtomicInteger();
        AtomicInteger otros = new AtomicInteger();
        CountDownLatch largada = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(vendedores);

        for (int i = 1; i <= vendedores; i++) {
            String vendedor = "Vendedor " + i;
            pool.submit(() -> {
                try {
                    largada.await();
                    ventaService.registrarVenta(new VentaRequest(clienteId, vendedor,
                            Arrays.asList(new ItemVentaRequest(p.getId(), 1))));
                    exitos.incrementAndGet();
                } catch (StockInsuficienteException e) {
                    sinStock.incrementAndGet();
                } catch (Exception e) {
                    otros.incrementAndGet();
                    e.printStackTrace();
                }
            });
        }
        largada.countDown();
        pool.shutdown();
        assertThat(pool.awaitTermination(30, TimeUnit.SECONDS)).isTrue();

        int stockFinal = productoRepository.findById(p.getId()).get().getStock();
        return new Resultado(exitos.get(), sinStock.get(), otros.get(), stockFinal);
    }

    private static class Resultado {
        final int exitos;
        final int sinStock;
        final int otrosErrores;
        final int stockFinal;

        Resultado(int exitos, int sinStock, int otrosErrores, int stockFinal) {
            this.exitos = exitos;
            this.sinStock = sinStock;
            this.otrosErrores = otrosErrores;
            this.stockFinal = stockFinal;
        }
    }
}
