package view;

import model.AffectationSalle;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AffectationTableModel extends AbstractTableModel {
    private List<AffectationSalle> affectations;
    private List<AffectationSalle> filteredAffectations;
    private final String[] columnNames = {"ID", "Nom Salle", "Classe", "Cours", "Jour", "Heure Début", "Heure Fin"};

    public AffectationTableModel() {
        this.affectations = new ArrayList<>();
        this.filteredAffectations = new ArrayList<>();
    }

    public void setAffectations(List<AffectationSalle> affectations) {
        this.affectations = new ArrayList<>(affectations); // Create a mutable copy
        this.filteredAffectations = new ArrayList<>(affectations);
        this.filteredAffectations.sort(Comparator.comparing(a -> a.getClasse().getNomClasse()));
        fireTableDataChanged();
    }

    public void filterAffectations(String query) {
        if (query == null || query.isEmpty()) {
            filteredAffectations = new ArrayList<>(affectations);
        } else {
            filteredAffectations = affectations.stream()
                .filter(a -> a.getClasse().getNomClasse().toLowerCase().contains(query.toLowerCase()) ||
                             a.getCours().getNomCours().toLowerCase().contains(query.toLowerCase()) ||
                             a.getSalle().getNomSalle().toLowerCase().contains(query.toLowerCase()) ||
                             a.getJour().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return filteredAffectations.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AffectationSalle affectation = filteredAffectations.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> affectation.getId();
            case 1 -> affectation.getSalle().getNomSalle();
            case 2 -> affectation.getClasse().getNomClasse();
            case 3 -> affectation.getCours().getNomCours();
            case 4 -> affectation.getJour();
            case 5 -> affectation.getHeureDebut();
            case 6 -> affectation.getHeureFin();
            default -> null;
        };
    }
}