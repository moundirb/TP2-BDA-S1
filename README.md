# TP2-BDA-S1
 Dashboard interface using Java to interact with a database through SQL

executeQuery("SELECT country.Name AS Name, country.Capital AS Capital, " +
        "GROUP_CONCAT(countrylanguage.Language SEPARATOR ', ') AS Languages FROM country " +
        "JOIN countrylanguage ON country.Code = countrylanguage.CountryCode GROUP BY country.Name, country.Capital");
