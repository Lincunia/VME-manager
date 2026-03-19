#include <gtk/gtk.h>

int main(int argc, char *argv[]) {
    GtkWidget *window, *notebook;
    GtkWidget *content, *label;
    GtkWidget *button1, *button2;
    GtkWidget *box_buttons;
    
    gtk_init(&argc, &argv);
    
    // Ventana principal
    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_title(GTK_WINDOW(window), "Pestañas con botones");
    gtk_window_set_default_size(GTK_WINDOW(window), 500, 300);
    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);
    
    // Crear el notebook
    notebook = gtk_notebook_new();
    gtk_container_add(GTK_CONTAINER(window), notebook);
    
    // --- Crear dos botones personalizados ---
    button1 = gtk_button_new_with_label("⚙️ Configuración");
    button2 = gtk_button_new_with_label("❓ Ayuda");
    
    // Conectar señales (opcional)
    g_signal_connect(button1, "clicked", G_CALLBACK(gtk_main_quit), NULL);
    g_signal_connect(button2, "clicked", G_CALLBACK(gtk_main_quit), NULL);
    
    // Colocar los botones en la parte SUPERIOR del panel de pestañas
    // GTK_PACK_START = izquierda, GTK_PACK_END = derecha
    gtk_notebook_set_action_widget(GTK_NOTEBOOK(notebook), button1, GTK_PACK_START);
    gtk_notebook_set_action_widget(GTK_NOTEBOOK(notebook), button2, GTK_PACK_END);
    
    // --- Crear algunas pestañas de ejemplo ---
    for (int i = 1; i <= 3; i++) {
        char buf[32];
        sprintf(buf, "Contenido de pestaña %d", i);
        content = gtk_label_new(buf);
        
        sprintf(buf, "Pestaña %d", i);
        label = gtk_label_new(buf);
        
        gtk_notebook_append_page(GTK_NOTEBOOK(notebook), content, label);
    }

    
    gtk_widget_show_all(window);
    gtk_main();
    
    return 0;
}
