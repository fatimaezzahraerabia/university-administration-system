package view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SalleView extends JTable {

                public SalleView() {
                    // Configurer l'apparence de la table
                    setShowHorizontalLines(true);
                    setGridColor(new Color(230, 230, 230));
                    setRowHeight(40);
                    setFont(new Font("sansserif", Font.PLAIN, 12));
                    getTableHeader().setReorderingAllowed(false);

                    // En-tête personnalisé
                    getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                            JLabel label = new JLabel(o.toString());
                            label.setOpaque(true);
                            label.setBackground(Color.WHITE);
                            label.setFont(new Font("sansserif", Font.BOLD, 12));
                            label.setForeground(new Color(102, 102, 102));
                            label.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
                            return label;
                        }
                    });

                    // Configurer le rendu des cellules
                    setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int row, int column) {
                            if (column == 4) {  // Supposons que la colonne 4 contienne le bouton "Modifier"
                                JButton button = new JButton("Modifier");
                                button.setBackground(new Color(34, 140, 209));
                                button.setForeground(Color.WHITE);
                                button.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        // Récupérer la liste des classes avec le statut "Réservé"
                                        listReservedClasses();
                                    }
                                });
                                return button;
                            } else {
                                return super.getTableCellRendererComponent(jtable, o, selected, bln1, row, column);
                            }
                        }
                    });
                }

                // Méthode pour ajouter une ligne
                public void addRow(Object[] row) {
                    DefaultTableModel model = (DefaultTableModel) getModel();
                    model.addRow(row);
                }

                // Méthode pour récupérer les classes avec le statut "Réservé"
                public void listReservedClasses() {
                    DefaultTableModel model = (DefaultTableModel) getModel();
                    StringBuilder reservedClasses = new StringBuilder("Classes réservées :\n");

                    // Parcourir les lignes du modèle et vérifier le statut de la classe
                    for (int i = 0; i < model.getRowCount(); i++) {
                        String status = (String) model.getValueAt(i, 3); // Supposons que la colonne 3 soit le statut

                        if ("Réservé".equals(status)) {
                            String className = (String) model.getValueAt(i, 0);  // Nom de la classe
                            reservedClasses.append(className).append("\n");
                        }
                    }

                    // Afficher les classes réservées dans une boîte de dialogue
                    JOptionPane.showMessageDialog(this, reservedClasses.toString());
                }


            }





