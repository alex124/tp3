package ligueBaseball;

import java.sql.*;
import java.util.List;

/**
 *  * @author Mathieu Lavoie, Alex Provencher et Vincent Gagnon
 * classe intermedaire entre l'usager et les object qui parle a la base de donnee.
 */
public class GestionEquipe {

	private Equipe equipe;
	private Terrain terrain;
	private Connexion cx;

	/**
	 * Creation d'une instance
	 */
	public GestionEquipe(Equipe equipe, Terrain terrain)
			throws LigueBaseballException {
		this.cx = equipe.getConnexion();
		this.equipe = equipe;
		this.terrain = terrain;
	}

	/**
	 * Ajout d'une nouvelle equipe dans la base de donnees. S'il existe deja,
	 * une exception est levee.
	 */
	public void ajout(String equipeNom) throws SQLException,
			LigueBaseballException, Exception {
		try {
			if (equipe.existe(equipeNom) != -1)
				throw new LigueBaseballException("Equipe existe deja: "
						+ equipeNom);
			int equipeId = equipe.maxJoueur();
			equipe.ajoutEquipe(equipeId, equipeNom);
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
		}
	}

	/**
	 * Ajout d'une nouvelle equipe dans la base de donnees. S'il existe deja,
	 * une exception est levee.
	 */
	public void ajout(String equipeNom, String nomTerrain, String adresseTerrain)
			throws SQLException, LigueBaseballException, Exception {
		try {
			if (equipe.existe(equipeNom) != -1)
				throw new LigueBaseballException("Equipe existe deja: "
						+ equipeNom);
			if (!terrain.existe(nomTerrain)) {
				int terrainId = terrain.maxTerrain();
				terrain.ajoutTerrain(terrainId, nomTerrain, adresseTerrain);
			}
			int equipeId = equipe.maxJoueur();
			int terrainId = terrain.getTerrain(nomTerrain);
			equipe.ajoutEquipe(equipeId, terrainId, equipeNom);
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
		}
	}

	/**
	 * Supprimer une equipe.
	 */
	public void supprimer(String equipeNom) throws SQLException,
			LigueBaseballException, Exception {
		try {
			if (equipe.existe(equipeNom) == -1)
				throw new LigueBaseballException("Equipe inexistante: "
						+ equipeNom);
			else{
				if (equipe.existeJoueurs(equipeNom)) {
					throw new LigueBaseballException(
							"Impossible de supprimer cette equipe ( " + equipeNom
									+ " ) car il y a des joueurs associes a cette equipe.");
				}
				else
					/* Suppression de l'equipe. */
					equipe.suppressionEquipe(equipeNom);
			}
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
		}
	}

	/**
 * 
 */
	/**
	 * fait les verification et envoie le mesage de rajouter l'equipe qui corespond aux parametres
	 */
	public void getEquipes() {
		try {
			List<TupleEquipe> listEquipes = equipe.getEquipes();
			for (TupleEquipe tupleEquipe : listEquipes) {
				System.out.println(tupleEquipe.equipeid + "\t"
						+ tupleEquipe.equipenom);
			}
		} catch (SQLException e) {
		    System.out.println("Erreur usager: l'equipe existe pas");
		}
	}
}
