-- Column: public.producto.stock_minimo

-- ALTER TABLE IF EXISTS public.producto DROP COLUMN IF EXISTS stock_minimo;

ALTER TABLE IF EXISTS public.producto
    ADD COLUMN stock_minimo integer NOT NULL DEFAULT 5;