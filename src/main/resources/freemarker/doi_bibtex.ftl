@data{${randomId},
  doi = {${doi}},
  url = {https://doi.org/${doi}},
  author = {<#list creators! as c>${c}; </#list>},
  publisher = {${publisher!}},
  title = {${titles[0].value}},
  year = {${publicationYear!}}
}
