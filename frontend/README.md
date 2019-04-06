laChanson
=========

Description
----------------------------------------------------
Mittelalterliche Simulation eines von Banditen bedrohten Dorfes


Build the static frontend
-------------------------
```bash
	cd frontend
```

generate the html

```bash
	npm start
```

show the html
```bash
	http-server ./dist &
```

optional: promote coming changes via grunt
```bash
	grunt watch
```


view http://localhost:8080

Deploy the static frontend
-------------------------

create a new netlify app
https://app.netlify.com/start

connect to your github repo

deploy your site

open "domain settings"-> "Custom Domains" -> "edit site name"
change the generated site name to "laChanson"

open https://laChanson.netlify.com/
