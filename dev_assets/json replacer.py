import json
from pathlib import Path

models = Path("models/")
for entry in models.iterdir():
    if (entry.is_dir()):
        submodels = entry.iterdir()
        for subentry in submodels:
            if (subentry.is_file() & subentry.name.endswith(".json") & ("vertical" not in subentry.name)):
                path = "models/" + entry.name + "/" + subentry.name

                model = open(path).read()
                model = model.replace("\"cullface\": \"north\"", "\"cullface\": \"down\"")

                f = open(path, "w")
                f.write(model)
                f.close()
