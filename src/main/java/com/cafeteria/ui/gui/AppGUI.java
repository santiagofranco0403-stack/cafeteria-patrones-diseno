package com.cafeteria.ui.gui;

import com.cafeteria.decorador.TipoAdicion;
import com.cafeteria.modelo.Producto;
import com.cafeteria.modelo.ProductoBase;
import com.cafeteria.pedido.EstadoPedido;
import com.cafeteria.pedido.Pedido;
import com.cafeteria.promocion.Promocion;
import com.cafeteria.servicio.CafeteriaService;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.Map;

/** Interfaz gráfica sencilla en Swing. Usa el mismo CafeteriaService que la consola. */
public class AppGUI extends JFrame {

    private final CafeteriaService servicio;
    private final DefaultListModel<ProductoBase> modeloMenu = new DefaultListModel<>();
    private final JList<ProductoBase> listaMenu = new JList<>(modeloMenu);
    private final Map<TipoAdicion, JCheckBox> casillasAdicion = new EnumMap<>(TipoAdicion.class);
    private final DefaultComboBoxModel<Pedido> modeloPedidos = new DefaultComboBoxModel<>();
    private final JComboBox<Pedido> comboPedidos = new JComboBox<>(modeloPedidos);
    private final DefaultListModel<String> modeloItems = new DefaultListModel<>();
    private final JLabel etiquetaTotal = new JLabel("Sin pedido seleccionado");
    private final JTextArea areaAvisos = new JTextArea(8, 60);

    public AppGUI(CafeteriaService servicio) {
        super("Cafetería - Sistema de pedidos");
        this.servicio = servicio;
        servicio.getMenu().getProductos().forEach(modeloMenu::addElement);
        servicio.getCocina().setSalida(this::mostrarAviso);
        servicio.getCaja().setSalida(this::mostrarAviso);
        servicio.getMesero().setSalida(this::mostrarAviso);

        setLayout(new BorderLayout(8, 8));
        add(panelSuperior(), BorderLayout.NORTH);
        add(panelMenu(), BorderLayout.WEST);
        add(panelPedido(), BorderLayout.CENTER);
        add(panelAvisos(), BorderLayout.SOUTH);

        comboPedidos.addActionListener(e -> refrescarPedido());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppGUI(CafeteriaService.porDefecto()).setVisible(true));
    }

    private JPanel panelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JSpinner mesa = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JButton nuevo = new JButton("Nuevo pedido");
        nuevo.addActionListener(e -> ejecutar(() -> {
            Pedido pedido = servicio.nuevoPedido((Integer) mesa.getValue());
            modeloPedidos.addElement(pedido);
            comboPedidos.setSelectedItem(pedido);
        }));
        JButton cierre = new JButton("Cierre de caja");
        cierre.addActionListener(e -> {
            JTextArea texto = new JTextArea(servicio.cierreDeCaja().comoTexto());
            texto.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(texto), "Cierre de caja",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        panel.add(new JLabel("Mesa:"));
        panel.add(mesa);
        panel.add(nuevo);
        panel.add(new JLabel("   Pedido:"));
        comboPedidos.setPreferredSize(new Dimension(320, 26));
        panel.add(comboPedidos);
        panel.add(cierre);
        return panel;
    }

    private JPanel panelMenu() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Menú"));
        panel.add(new JScrollPane(listaMenu), BorderLayout.CENTER);

        JPanel adiciones = new JPanel();
        adiciones.setLayout(new BoxLayout(adiciones, BoxLayout.Y_AXIS));
        adiciones.add(new JLabel("Adiciones:"));
        for (TipoAdicion tipo : TipoAdicion.values()) {
            JCheckBox casilla = new JCheckBox(tipo.getEtiqueta());
            casillasAdicion.put(tipo, casilla);
            adiciones.add(casilla);
        }
        JButton agregar = new JButton("Agregar producto");
        agregar.addActionListener(e -> ejecutar(this::agregarSeleccionado));
        JButton desayuno = new JButton("Combo desayuno");
        desayuno.addActionListener(e -> ejecutar(() -> agregar(servicio.getDirector().comboDesayuno())));
        JButton almuerzo = new JButton("Combo almuerzo");
        almuerzo.addActionListener(e -> ejecutar(() -> agregar(servicio.getDirector().comboAlmuerzo())));
        adiciones.add(agregar);
        adiciones.add(desayuno);
        adiciones.add(almuerzo);
        panel.add(adiciones, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(330, 400));
        return panel;
    }

    private JPanel panelPedido() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Pedido seleccionado"));
        panel.add(new JScrollPane(new JList<>(modeloItems)), BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(2, 3, 4, 4));
        botones.add(botonEstado("Enviar a cocina", EstadoPedido.EN_PREPARACION));
        botones.add(botonEstado("Marcar listo", EstadoPedido.LISTO));
        botones.add(botonEstado("Entregar", EstadoPedido.ENTREGADO));
        JButton cobrar = new JButton("Cobrar");
        cobrar.addActionListener(e -> ejecutar(() -> {
            Promocion promocion = servicio.cobrar(pedidoActual().getNumero());
            JOptionPane.showMessageDialog(this, "Promoción aplicada: " + promocion.getNombre()
                    + "\nTotal: $" + pedidoActual().getTotal());
        }));
        botones.add(cobrar);
        botones.add(botonEstado("Cancelar", EstadoPedido.CANCELADO));

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(etiquetaTotal, BorderLayout.NORTH);
        sur.add(botones, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel panelAvisos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Avisos a cocina, caja y mesero"));
        areaAvisos.setEditable(false);
        panel.add(new JScrollPane(areaAvisos), BorderLayout.CENTER);
        return panel;
    }

    private JButton botonEstado(String texto, EstadoPedido estado) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> ejecutar(() -> servicio.cambiarEstado(pedidoActual().getNumero(), estado)));
        return boton;
    }

    private void agregarSeleccionado() {
        ProductoBase base = listaMenu.getSelectedValue();
        if (base == null) {
            throw new IllegalStateException("Selecciona un producto del menú");
        }
        Producto producto = base;
        for (Map.Entry<TipoAdicion, JCheckBox> entrada : casillasAdicion.entrySet()) {
            if (entrada.getValue().isSelected() && entrada.getKey().aplicaA(producto)) {
                producto = entrada.getKey().aplicar(producto);
            }
            entrada.getValue().setSelected(false);
        }
        agregar(producto);
    }

    private void agregar(Producto producto) {
        servicio.agregarProducto(pedidoActual().getNumero(), producto);
    }

    private Pedido pedidoActual() {
        Pedido pedido = (Pedido) comboPedidos.getSelectedItem();
        if (pedido == null) {
            throw new IllegalStateException("Primero crea o selecciona un pedido");
        }
        return pedido;
    }

    private void ejecutar(Runnable accion) {
        try {
            accion.run();
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudo completar",
                    JOptionPane.WARNING_MESSAGE);
        }
        refrescarPedido();
    }

    private void refrescarPedido() {
        modeloItems.clear();
        Pedido pedido = (Pedido) comboPedidos.getSelectedItem();
        if (pedido == null) {
            etiquetaTotal.setText("Sin pedido seleccionado");
            return;
        }
        pedido.getItems().forEach(p -> modeloItems.addElement(p.toString()));
        String promo = pedido.getEstado() == EstadoPedido.PAGADO
                ? pedido.getPromocionAplicada()
                : servicio.promocionVigente(pedido.getNumero()).getNombre() + " (vigente)";
        etiquetaTotal.setText("<html>Estado: <b>" + pedido.getEstado() + "</b> | Subtotal: $"
                + pedido.getSubtotal() + " | Promoción: " + promo + " | Total: $" + pedido.getTotal() + "</html>");
        comboPedidos.repaint();
    }

    private void mostrarAviso(String mensaje) {
        areaAvisos.append(mensaje + "\n");
    }
}
