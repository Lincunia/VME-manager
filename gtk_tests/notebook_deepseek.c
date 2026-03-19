#include <gtk/gtk.h>

// Función callback para cuando se cambia de pestaña
static void on_tab_switch(GtkNotebook *notebook, GtkWidget *page, guint page_num, gpointer data) {
    g_print("Cambió a la pestaña %d\n", page_num);
}

int main(int argc, char *argv[]) {
    GtkWidget *window;
    GtkWidget *notebook;
    GtkWidget *label1, *label2, *label3;
    GtkWidget *content1, *content2, *content3;
    
    gtk_init(&argc, &argv);
    
    // Crear ventana principal
    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_title(GTK_WINDOW(window), "Panel con Pestañas");
    gtk_window_set_default_size(GTK_WINDOW(window), 400, 300);
    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);
    
    // Crear el notebook (panel de pestañas)
    notebook = gtk_notebook_new();
    gtk_container_add(GTK_CONTAINER(window), notebook);
    
    // --- PESTAÑA 1 ---
    content1 = gtk_box_new(GTK_ORIENTATION_VERTICAL, 5);
    gtk_box_pack_start(GTK_BOX(content1), 
                      gtk_label_new("Contenido de la pestaña 1"), 
                      TRUE, TRUE, 0);
    
    label1 = gtk_label_new("Pestaña 1");
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), content1, label1);
    
    // --- PESTAÑA 2 ---
    content2 = gtk_box_new(GTK_ORIENTATION_VERTICAL, 5);
    GtkWidget *button = gtk_button_new_with_label("Botón en pestaña 2");
    gtk_box_pack_start(GTK_BOX(content2), button, TRUE, TRUE, 0);
    
    label2 = gtk_label_new("Pestaña 2");
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), content2, label2);
    
    // --- PESTAÑA 3 ---
    content3 = gtk_box_new(GTK_ORIENTATION_VERTICAL, 5);
    gtk_box_pack_start(GTK_BOX(content3), 
                      gtk_label_new("Contenido de la pestaña 3"), 
                      TRUE, TRUE, 0);
    
    label3 = gtk_label_new("Pestaña 3");
    gtk_notebook_append_page(GTK_NOTEBOOK(notebook), content3, label3);
    
    // Conectar señal para cambio de pestaña
    g_signal_connect(notebook, "switch-page", G_CALLBACK(on_tab_switch), NULL);
    
    // Mostrar todo
    gtk_widget_show_all(window);
    
    gtk_main();
    
    return 0;
}
