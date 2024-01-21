INSERT INTO COMPANIES (company_name) VALUES
      ('IBM'),
      ('Barclays'),
      ('Fairfield & Company');

INSERT INTO EMPLOYEES (first_name, last_name, email, company_id) VALUES
      ('Dan', 'Vega', 'danvega@gmail.com', SELECT company_id FROM COMPANIES WHERE company_name = 'IBM'),
      ('Jen', 'Vega', 'jenvega@email.com', SELECT company_id FROM COMPANIES WHERE company_name = 'Barclays'),
      ('Bella', 'Vega', 'bellavega@gmail.com', SELECT company_id FROM COMPANIES WHERE company_name = 'Fairfield & Company');



