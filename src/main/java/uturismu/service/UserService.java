/*
 * This file is part of "U Turismu" project. 
 * 
 * U Turismu is an enterprise application in support of calabrian tour operators.
 * This system aims to promote tourist services provided by the operators
 * and to develop and improve tourism in Calabria.
 *
 * Copyright (C) 2012 "LagrecaSpaccarotella" team.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uturismu.service;

import java.util.List;

import uturismu.dto.Account;
import uturismu.dto.Booker;
import uturismu.dto.HolidayPackage;
import uturismu.dto.TourOperator;

/**
 * Questa interfaccia rappresenta il servizio per la gestione delle funzionalità
 * dell'utente generico. Tutto ciò che riguarda la creazione dell'account, la
 * modifica o la disattivazione, nonché la visualizzazione delle offerte
 * turistiche ancora valide, viene gestito da questo servizio.
 * 
 * @author "LagrecaSpaccarotella" team.
 */
public interface UserService {

	/**
	 * Crea l'account dell'utente <b>Booker</b>.
	 * 
	 * @param account
	 *           rappresenta l'account dell'utente.
	 * @param booker
	 *           contiene le informazioni anagrafiche.
	 */
	public void createAccount(Account account, Booker booker);

	/**
	 * Crea l'account dell'utente <b>Tour Operator</b>.
	 * 
	 * @param account
	 *           rappresenta l'account dell'utente
	 * @param tourOperator
	 *           contiene le informazioni anagrafiche.
	 */
	public void createAccount(Account account, TourOperator tourOperator);

	/**
	 * Effettua il login di un qualsiasi utente registrato al sistema.
	 * 
	 * @param email
	 *           l'indirizzo di posta elettronica utilizzato come username.
	 * @param password
	 *           la password utilizzata per accedere al sistema.
	 * @return restituisce l'oggetto {@link Account} appena creato.
	 * @throws InvalidCredentialException
	 *            viene lanciata quando si tenta di accedere con delle
	 *            credenziali errate (email e/o password errata)
	 */
	public Account logIn(String email, String password);

	/**
	 * Disattiva l'account.
	 * 
	 * @param account
	 *           l'account da disattivare
	 */
	public void deactivateAccount(Account account);

	/**
	 * Restituisce gli holiday package <b>pubblici</b> di tutti i tour operator.
	 * 
	 * @return {@link List}<{@link HolidayPackage}>
	 */
	public List<HolidayPackage> getHolidayPackages();

	/**
	 * Restituisce il numero di holiday package <b>pubblici</b> di tutti i tour
	 * operator.
	 * 
	 * @return {@link Integer}
	 */
	public Long getHolidayPackagesNumber();

	/**
	 * Restituisce gli holiday package <b>pubblici</b> dello specifico tour
	 * operator.
	 * 
	 * @param id
	 *           l'id del tour operator.
	 * @return {@link List}<{@link HolidayPackage}>
	 */
	public List<HolidayPackage> getHolidayPackagesByTourOperator(Long id);

	/**
	 * Restituisce gli holiday package <b>pubblici</b> annotati con i tag passati
	 * in input al metodo.
	 * 
	 * @param tags
	 *           un array di tag id
	 * @return {@link List}<{@link HolidayPackage}>
	 */
	public List<HolidayPackage> getHolidayPackagesByTags(Long... tags);

	/**
	 * Restituisce i tour operator registrati al sito.
	 * 
	 * @return {@link List}<{@link TourOperator}>
	 */
	public List<TourOperator> getTourOperators();

	/**
	 * Restitusce un {@link TourOperator} in base al suo id.
	 * 
	 * @param id
	 *           l'id del touroperator
	 * @return {@link TourOperator}
	 */
	public TourOperator getTourOperatorById(Long id);

	/**
	 * Restitusce un {@link HolidayPackage} dato il suo id.
	 * 
	 * @param id
	 *           l'id dell'holiday package
	 * @return {@link HolidayPackage}
	 */
	public HolidayPackage getHolidayPackageByID(Long id);

	/**
	 * Restituisce un {@link Booker} dato il suo id.
	 * 
	 * @param id
	 *           l'id del Booker
	 * @return {@link Booker}
	 */
	public Booker getBookerById(Long id);

	/**
	 * Restituisce un {@link Account} con una determinata email. Se l'email non
	 * esiste, restituisce <code>null</code>
	 * 
	 * @param email
	 *           l'email da ricercare
	 * @return {@link Account}
	 */
	public Account getAccountByEmail(String email);

	/**
	 * Aggiorna un tour operator ed il suo account
	 * @param account
	 * @param tourOperator
	 */
	public void update(Account account,TourOperator tourOperator);
	
	/**
	 * Aggiorna un booker ed il suo account
	 * @param account
	 * @param booker
	 */
	public void update(Account account, Booker booker) ;
}