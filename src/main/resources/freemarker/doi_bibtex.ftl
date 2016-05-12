@data{${randomId},
  doi = {${doi}},
  url = {http://dx.doi.org/${doi}},
  author = {${creators?join(" and ")}},
  publisher = {${publisher!}},
  title = {${titles[0].value}},
  year = {${publicationYear!}}
}
