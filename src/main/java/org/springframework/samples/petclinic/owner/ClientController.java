package org.springframework.samples.petclinic.owner;

import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
class ClientController {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "clients/createOrUpdateOwnerForm";
    private final ClientRepository clients;


    public ClientController(ClientRepository clinicService) {
        this.clients = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/clients/new")
    public String initCreationForm(Map<String, Object> model) {
        Client client = new Client();
        model.put("client", client);
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/clients/new")
    public String processCreationForm(@Valid Client client, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            this.clients.save(client);
            return "redirect:/clients/" + client.getId();
        }
    }

    @GetMapping("/clients/find")
    public String initFindForm(Map<String, Object> model) {
        model.put("client", new Client());
        return "clients/findOwners"; //modifica
    }

    @GetMapping("/clients")
    public String processFindForm(Client client, BindingResult result, Map<String, Object> model) {

        // allow parameterless GET request for /owners to return all records
        if (client.getCompanyName() == null) {
            client.setCompanyName(""); // empty string signifies broadest possible search
        }

        // find owners by last name
        Collection<Client> results = this.clients.findByCompanyName(client.getCompanyName());
        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("companyName", "notFound", "not found");
            return "clients/findOwners"; //modifica
        } else if (results.size() == 1) {
            // 1 owner found
            client = results.iterator().next();
            return "redirect:/clients/" + client.getId();
        } else {
            // multiple owners found
            model.put("selections", results);
            return "clients/ownersList"; //modifica
        }
    }

    @GetMapping("/clients/{clientId}/edit")
    public String initUpdateOwnerForm(@PathVariable("clientId") int ownerId, Model model) { //modifica
        Client client = this.clients.findById(ownerId);
        model.addAttribute(client);
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/clients/{clientId}/edit")
    public String processUpdateOwnerForm(@Valid Client client, BindingResult result, @PathVariable("clientId") int clientId) { //modifica
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            client.setId(clientId);
            this.clients.save(client);
            return "redirect:/clients/{clientId}";
        }
    }

    /**
     * Custom handler for displaying an owner.
     *
     * @param ownerId the ID of the owner to display
     * @return a ModelMap with the model attributes for the view
     */
    @GetMapping("/clients/{clientId}")
    public ModelAndView showOwner(@PathVariable("clientId") int clientId) {
        ModelAndView mav = new ModelAndView("clients/clientDetails");
        mav.addObject(this.clients.findById(clientId));
        return mav;
    }

}
